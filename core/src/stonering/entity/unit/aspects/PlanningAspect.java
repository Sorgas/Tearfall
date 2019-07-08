package stonering.entity.unit.aspects;

import stonering.entity.job.Task;
import stonering.entity.job.action.Action;
import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.entity.PositionAspect;
import stonering.game.GameMvc;
import stonering.game.model.lists.tasks.TaskContainer;
import stonering.util.geometry.Position;
import stonering.entity.unit.Unit;
import stonering.util.global.Logger;

import java.util.ArrayList;

/**
 * Holds current creature's task and it's steps. resolves behavior, if some step fails.
 * Selects new tasks.
 * Updates target for movement on task switching.
 *
 * @author Alexander Kuzyakov on 10.10.2017.
 */
public class PlanningAspect extends Aspect {
    public final static String NAME = "planning";
    private Task task;
    private Position target;
    private Action action; // is set while performing action.
    private boolean exactTarget;

    public PlanningAspect(Entity entity) {
        super(entity);
    }

    /**
     * There are three states of unit:
     * 1. with no task
     * 2. with task moving to target
     * 3. with task and in target, performing action.
     */
    public void turn() {
        if (action == null) {
            if (!isTaskInProgress()) selectTask();// try find task, check it and claim
            if (task == null) return;
            if (!task.getNextAction().getActionTarget().check(getEntityPosition()))
                return; // actor not in position, keep moving
            if (!checkActionSequence()) { // check all actions in a sequence before performing
                Logger.TASKS.logDebug("Checking of " + task + " failed.");
                return;
            }
            action = task.getNextAction();
        }
        if (action != null) { // currently performing
            if (!action.getActionTarget().check(getEntityPosition())) return; // actor not in position, keep moving
            if (action.perform()) { // act. called several times
                System.out.println("action " + action.toString() + "performed");
                action = null;
                target = task.isFinished() ? null : task.getNextAction().getActionTarget().getPosition();
            }
        }
    }

    /**
     * Checks if task can be performed.
     * During this method requirement aspects create additional action.
     *
     * @return false, if some action in sequence cannot be performed.
     */
    private boolean checkActionSequence() {
        if (task == null) return false;
        while (task.getNextAction().check() == Action.NEW) {
            Logger.TASKS.logDebug(task.getNextAction().toString() + " created on checking sequence.");
        }
        return task.getNextAction().check() == Action.OK;
    }

    /**
     * Checks if unit has no task or current is finished.
     * Finished tasks remove themselves from container.
     */
    private boolean isTaskInProgress() {
        return task != null && !task.isFinished();
    }

    /**
     * Finds appropriate task for this performer.
     * Checks priorities of all available tasks.
     * After this method task is updated.
     * TODO combat tasks
     */
    private void selectTask() {
        reset();
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(takeTaskFromNeedsAspect());
        tasks.add(getTaskFromContainer());
        for (Task task : tasks) { // selects task with highest priority
            if (task != null && (this.task == null || this.task.getPriority() < task.getPriority()))
                this.task = task;
        }
        if (task == null) return;
        task.setPerformer((Unit) entity);

        if (checkActionSequence()) {
            Logger.TASKS.logDebug("Task " + task.getName() + " has taken by " + entity.toString() + ".");
            if (task.getNextAction() != null) target = task.getNextAction().getActionTarget().getPosition();
        } else {
            Logger.TASKS.logDebug("Initial checking of " + task + " failed.");
            reset(); // if requirements are not met.
        }
    }

    /**
     * Calls NeedAspect to create task for satisfying strongest need.
     * Can return null.
     */
    private Task takeTaskFromNeedsAspect() {
        NeedsAspect needsAspect = entity.getAspect(NeedsAspect.class);
        if (needsAspect == null || needsAspect.getStrongestNeed() == null)
            return null; // no needs at all, or no strong needs
        return needsAspect.getStrongestNeed().tryCreateTask(entity);
    }

    /**
     * Calls TaskContainer to find appropriate task for this actor and his position.
     * Can return null.
     */
    private Task getTaskFromContainer() {
        return GameMvc.instance().getModel().get(TaskContainer.class).getActiveTask(getEntityPosition());
    }

    /**
     * Resets state of task and this aspect.
     */
    public void reset() {
        if (task == null) return;
        Logger.TASKS.logDebug("Resetting planning aspect of " + toString());
        task.reset();
        task = null;
        target = null;
    }

    public boolean isTargetExact() {
        if (task != null) {
            return task.getNextAction().getActionTarget().isExactTarget();
        }
        return false;
    }

    public Position getTarget() {
        return target;
    }

    private Position getEntityPosition() {
        return entity.getAspect(PositionAspect.class).position;
    }
}