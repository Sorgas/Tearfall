package stonering.entity.unit.aspects;

import stonering.entity.job.Task;
import stonering.entity.job.action.Action;
import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.entity.unit.aspects.needs.NeedsAspect;
import stonering.game.GameMvc;
import stonering.game.model.system.tasks.TaskContainer;
import stonering.util.geometry.Position;
import stonering.entity.unit.Unit;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

import static stonering.entity.job.action.target.ActionTargetStatusEnum.WAIT;

/**
 * Holds current creature's task. Task itself, is a sequence of actions with pointer to active action.
 * Has flag for indicating whether movement is needed for performing task.
 * Flag is false if task is null, or target of an action is reached.
 * Selects new tasks from container and creature needs.
 *
 * @author Alexander Kuzyakov on 10.10.2017.
 */
public class PlanningAspect extends Aspect {
    private Task task;
    private boolean movementNeeded;

    public PlanningAspect(Entity entity) {
        super(entity);
    }

    public void turn() {
        if (hasNoActiveTask() && !trySelectTask()) return; // no active task, and no new found
        switch (task.nextAction.getActionTarget().check(entity.position)) { // creates actions
            case FAIL: // checking failed
                updateState(null);
                return;
            case NEW: // new action created
                updateState(task); // creates actions
                return;
            case WAIT: // keep moving to target
                return;
            case READY: // target reached
                if (!task.nextAction.perform()) return; // keep performing action
                updateState(task); // update state after finishing action
        }
    }

    /**
     * Checks if unit has no task or current is finished.
     * Finished tasks remove themselves from container, so only link nullifying is needed.
     */
    private boolean hasNoActiveTask() {
        if (task != null && task.isFinished()) updateState(null); // free finished task
        return task == null;
    }

    /**
     * Finds appropriate task for this performer.
     * Checks priorities of all available tasks.
     * After this method task is updated.
     * TODO combat tasks
     * TODO non possible tasks with high priority can block other tasks
     */
    private boolean trySelectTask() {
        ArrayList<Task> tasks = new ArrayList<>();
        if (entity.hasAspect(NeedsAspect.class)) tasks.add(entity.getAspect(NeedsAspect.class).satisfyingTask);
        tasks.add(getTaskFromContainer());
        Task task = tasks.stream()
                .filter(Objects::nonNull)
                .max(Comparator.comparingInt(task1 -> task1.priority))
                .orElse(null); // task with max priority
        return updateState(task); // claim task, if any
    }

    /**
     * Changes state of this aspect to given task. Passing null means no task is performed.
     * With finished task, state of this aspect is reset.
     * New tasks for this aspect should be added with this.
     */
    public boolean updateState(Task newTask) {
        if (newTask != null) {
            Logger.TASKS.logDebug("Checking of task " + newTask.toString() + " for " + entity.toString());
            newTask.performer = (Unit) entity; // performer is required for checking
            if (checkActionSequence(newTask)) { // valid task
                this.task = newTask;
                movementNeeded = task.nextAction.actionTarget.check(entity.position) == WAIT;
                return true;
            }
        }
        // clear state or invalid task
        if (newTask != null) newTask.reset(); // reset created action sequence in invalid task
        task = null; // free this aspect
        movementNeeded = false;
        return false;
    }

    /**
     * Checks if task can be performed.
     * In this method requirement aspects of actions create additional actions.
     *
     * @return false, if some action in sequence cannot be performed.
     */
    private boolean checkActionSequence(Task task) {
        if (task.isFinished()) return false;
        int result;
        while ((result = task.nextAction.check()) == Action.NEW) { // can create sub actions
        }
        return result == Action.OK;
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

    private Task getTaskFromContainer() {
        return GameMvc.instance().getModel().get(TaskContainer.class).getActiveTask((Unit) entity);
    }

    public Position getTarget() {
        return task != null ? task.nextAction.actionTarget.getPosition() : null;
    }

    public boolean isMovementNeeded() {
        return movementNeeded;
    }

    public Task getTask() {
        return task;
    }
}