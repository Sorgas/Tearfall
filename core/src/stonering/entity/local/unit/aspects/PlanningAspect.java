package stonering.entity.local.unit.aspects;

import stonering.entity.jobs.Task;
import stonering.entity.jobs.actions.Action;
import stonering.entity.local.Aspect;
import stonering.entity.local.AspectHolder;
import stonering.game.GameMvc;
import stonering.game.model.lists.tasks.TaskContainer;
import stonering.util.geometry.Position;
import stonering.entity.local.unit.Unit;
import stonering.util.global.TagLoggersEnum;

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
    private Task currentTask;
    private boolean movementNeeded = false; // true, when has task and not in position.

    public PlanningAspect(AspectHolder aspectHolder) {
        super(aspectHolder);
    }

    public void turn() {
        if (!checkTask()) selectTask();// try find task, check it and claim
        if (checkActionSequence()) { // check all actions in a sequence.
            if (!(movementNeeded = !currentTask.getNextAction().getActionTarget().check(aspectHolder.getPosition()))) { // actor on position, so movement is not needed
                String actionName = currentTask.getNextAction().toString();
                if (currentTask.getNextAction().perform()) { // act. called several times
                    TagLoggersEnum.TASKS.logDebug(aspectHolder + " completes action " + actionName);
                    System.out.println("check");
                }
            }
        }
        // keep moving to target
    }

    /**
     * Checks if task can be performed.
     * During this method requirement aspects create additional actions.
     *
     * @return false, if some action in sequence cannot be performed.
     */
    private boolean checkActionSequence() {
        if (currentTask == null) return false;
        Action currentAction;
        boolean lastCheck;
        do {
            currentAction = currentTask.getNextAction();
            lastCheck = currentAction.check(); // can create additional actions
        }
        while (currentAction != currentTask.getNextAction()); // no additional actions created, return check result of last action.
        return lastCheck;
    }

    /**
     * Task taken and not yet finished.
     *
     * @return
     */
    private boolean checkTask() {
        return currentTask != null && !currentTask.isFinished();
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
            if (task == null) continue;
            if (currentTask == null) {
                currentTask = task;
                continue;
            }
            currentTask = currentTask.getPriority() > task.getPriority() ? currentTask : task;
        }
        if (currentTask == null) return;
        claimTask();
        // initial task checking.
        if (!checkActionSequence()) reset(); // if requirements are not met.
    }

    /**
     * Calls NeedAspect to create task for satisfying strongest need.
     * Can return null.
     */
    private Task takeTaskFromNeedsAspect() {
        NeedsAspect needsAspect = aspectHolder.getAspect(NeedsAspect.class);
        if (needsAspect == null || needsAspect.getStrongestNeed() == null)
            return null; // no needs at all, or no strong needs
        return needsAspect.getStrongestNeed().tryCreateTask(aspectHolder);
    }

    /**
     * Calls TaskContainer to find appropriate task for this actor and his position.
     * Can return null.
     */
    private Task getTaskFromContainer() {
        return GameMvc.instance().getModel().get(TaskContainer.class).getActiveTask(aspectHolder.getPosition());
    }

    /**
     * Sets this actor as performer to taken task.
     */
    private void claimTask() {
        currentTask.setPerformer((Unit) aspectHolder);
        TagLoggersEnum.TASKS.logDebug("Task " + currentTask.getName() + " has taken by " + aspectHolder.toString() + ".");
    }

    /**
     * Resets state of task and this aspect.
     */
    public void reset() {
        Task task = currentTask;
        currentTask = null;
        movementNeeded = false;
        if (task != null) task.reset();
    }
    public boolean isTargetExact() {
        if (currentTask != null) {
            return currentTask.getNextAction().getActionTarget().isExactTarget();
        }
        return false;
    }

    public Position getTarget() {
        return currentTask != null && currentTask.getNextAction() != null ?
                currentTask.getNextAction().getActionTarget().getPosition() :
                null;
    }

    public boolean isMovementNeeded() {
        return movementNeeded;
    }
}