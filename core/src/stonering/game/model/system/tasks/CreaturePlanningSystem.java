package stonering.game.model.system.tasks;

import stonering.entity.job.ItemOrderTask;
import stonering.entity.job.Task;
import stonering.entity.job.action.Action;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.PlanningAspect;
import stonering.entity.unit.aspects.needs.NeedsAspect;
import stonering.enums.TaskStatusEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.items.ItemContainer;
import stonering.game.model.system.units.UnitContainer;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

import static stonering.entity.job.action.target.ActionTargetStatusEnum.WAIT;

/**
 * System for giving {@link Task}s to {@link Unit}s.
 * Tasks are stored in units {@link PlanningAspect}.
 *
 * @author Alexander on 28.10.2019.
 */
public class CreaturePlanningSystem {

    public void update(Unit unit) {
        PlanningAspect aspect = unit.getAspect(PlanningAspect.class);
        if (hasNoActiveTask(aspect) && !trySelectTask()) return; // no active task, and no new found
        Task task = aspect.task;
        switch (task.nextAction.getActionTarget().check(unit.position)) { // creates actions
            case FAIL: // checking failed
                GameMvc.instance().getModel().get(UnitContainer.class).planningSystem.failTask(task);
                updateState(null);
                return;
            case NEW: // new action created
                updateState(task); // creates actions
                return;
            case WAIT: // keep moving to target
                return;
            case READY: // target reached
                if (task.nextAction.perform()) updateState(task); // update state after finishing action
        }
    }


    /**
     * Checks if unit has no task or current is finished.
     * Finished tasks remove themselves from container, so only link nullifying is needed.
     */
    private boolean hasNoActiveTask(PlanningAspect aspect) {
        if (aspect.task != null && aspect.task.isFinished()) updateState(null); // free finished task
        return aspect.task == null;
    }

    /**
     * Finds appropriate task for this performer.
     * Checks priorities of all available tasks.
     * After this method task is updated.
     * TODO combat tasks
     * TODO non possible tasks with high priority can block other tasks
     * @return true, if any new non-null task found;
     */
    private boolean trySelectTask(Unit unit) {
        TaskContainer container = GameMvc.instance().getModel().get(TaskContainer.class);
        ArrayList<Task> tasks = new ArrayList<>();
        if (unit.hasAspect(NeedsAspect.class)) tasks.add(unit.getAspect(NeedsAspect.class).satisfyingTask); // needs can generate tasks
        tasks.add(container.getActiveTask(unit));
        Task task = tasks.stream()
                .filter(Objects::nonNull)
                .max(Comparator.comparingInt(task1 -> task1.priority))
                .orElse(null); // task with max priority
        return task != null &&
                container.claimTask(task) &&
                updateState(unit.getAspect(PlanningAspect.class), task); // claim task, and set its performer to this.
    }


    /**
     * Changes state of this aspect to given task. Passing null means no task is performed.
     * With finished task, state of this aspect is reset.
     * New tasks for this aspect should be added with this.
     */
    public boolean updateState(PlanningAspect aspect, Task newTask) {
        Unit unit = (Unit) aspect.getEntity();
        if (newTask != null) {
            Logger.TASKS.logDebug("Checking of task " + newTask.toString() + " for " + unit);
            newTask.performer = unit; // performer is required for checking
            if (checkActionSequence(newTask)) { // valid task, finished or invalid one won't go
                aspect.task = newTask;
                aspect.movementNeeded = aspect.task.nextAction.actionTarget.check(unit.position) == WAIT;
                return true;
            }
        }
        // clear state or invalid task
        if (newTask != null) newTask.reset(); // reset created action sequence in invalid task
        aspect.task = null; // free this aspect
        aspect.movementNeeded = false;
        return false;
    }

    /**
     * Checks if task can be performed.
     * In this method requirement aspects of actions create additional actions.
     *
     * @return false, if some action in sequence cannot be performed.
     */
    private boolean checkActionSequence(Task task) {
        if (task.isFinished()) {
            GameMvc.instance().getModel().get(UnitContainer.class).planningSystem.finishTask(task);
            return false;
        }
        int result;
        while ((result = task.nextAction.check()) == Action.NEW) { // can create sub actions
        }
        if (result != Action.OK) {
            GameMvc.instance().getModel().get(UnitContainer.class).planningSystem.failTask(task);
            return false;
        }
        return true;
    }

    /**
     * For cancelling task, caused by external factor (path blocking, enemy, player).
     */
    public void interrupt() {
        if (task == null) return;
        Logger.TASKS.logDebug("Resetting planning aspect of " + toString());
        Task task = this.task;
        updateState(null);
        task.reset();
    }


    public void assignTaskToUnit(Task task, Unit unit) {
        if(!GameMvc.instance().getModel().get(TaskContainer.class).claimTask(task)) return; // lock task in container
        unit.getAspect(PlanningAspect.class).updateState(task); // give task to unit
    }

    /**
     * Finished tasks are removed completely.
     */
    public void finishTask(Task task) {
        removeTask(task);
        if(task instanceof ItemOrderTask) setItemOrderStatus((ItemOrderTask) task, TaskStatusEnum.COMPLETE);
    }

    /**
     * Failed tasks
     */
    public void failTask(Task task) {
        removeTask(task);
        if(task instanceof ItemOrderTask) setItemOrderStatus((ItemOrderTask) task, TaskStatusEnum.FAILED);
    }

    /**
     * Completely removes task from game. Used for finished and failed tasks. Repeated tasks in workbenches are handled separately.
     * Task should be assigned in {@link TaskContainer} ans have performer.
     */
    public void removeTask(Task task) {
        if(task.performer != null) {
            TaskContainer container = GameMvc.instance().getModel().get(TaskContainer.class);
            if(!container.assignedTasks.remove(task)) Logger.TASKS.logError("Finished task was not assigned."); // remove from container
            GameMvc.instance().getModel().get(ItemContainer.class).freeItems(task.lockedItems); // free items
            task.performer.getAspect(PlanningAspect.class).updateState(null); // free unit
        } else {
            // TODO remove not assigned tasks.
        }
    }

    private void setItemOrderStatus(ItemOrderTask task, TaskStatusEnum status) {
        task.order.status = status;
    }
}
