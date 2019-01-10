package stonering.entity.local.unit.aspects;

import stonering.entity.jobs.Task;
import stonering.entity.jobs.actions.Action;
import stonering.entity.local.Aspect;
import stonering.entity.local.AspectHolder;
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
    public static String NAME = "planning";
    private Task currentTask;
    private boolean movementNeeded = false; // true, when has task and not in position.

    public PlanningAspect(AspectHolder aspectHolder) {
        super(aspectHolder);
    }

    @Override
    public String getName() {
        return NAME;
    }

    /**
     * Called from {@link AspectHolder}
     */
    public void turn() {
        if (checkTask()) {
            if (!(movementNeeded = !currentTask.getNextAction().getTargetAspect().check(aspectHolder.getPosition()))) { // actor on position, so movement is not needed
                if (checkActionSequence()) {
                    if (currentTask.getNextAction().perform()) { // act. called several times
                        TagLoggersEnum.TASKS.logDebug(aspectHolder.toString() + " completes another action.");
                    }
                }
            }
            // keep moving to target
        } else {
            selectTask();// try find task, check it and claim
            if (currentTask != null) {
                TagLoggersEnum.TASKS.logDebug("task " + currentTask.getName() + " selected by " + aspectHolder.toString());
            }
        }
    }

    /**
     * Checks if task can be performed. That requires requirement aspects to be checked with true.
     * During this method requirement aspects create additional actions.
     *
     * @return
     */
    private boolean checkActionSequence() {
        Action currentAction;
        boolean lastCheck;
        do {
            currentAction = currentTask.getNextAction();
            lastCheck = currentAction.getRequirementsAspect().check(); // can create additional actions
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
        freeTask();
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(takeTaskFromNeedsAspect());
        tasks.add(getTaskFromContainer());
        for (Task task : tasks) {
            if (task != null) {
                if (currentTask != null) {
                    currentTask = currentTask.getPriority() > task.getPriority() ? currentTask : task;
                } else {
                    currentTask = task;
                }
            }
        }
        if (currentTask != null) {
            claimTask();
            if (!checkActionSequence()) { // initial task checking.
                freeTask(); // if requirements are not met.
            }
        }
    }

    /**
     * Calls NeedAspect to create task for satisfying strongest need.
     * Can return null;
     *
     * @return
     */
    private Task takeTaskFromNeedsAspect() {
        NeedsAspect needsAspect = ((NeedsAspect) aspectHolder.getAspects().get(NeedsAspect.NAME));
        Task needTask = null;
        if (needsAspect != null) {
            needsAspect.update();
            needTask = needsAspect.getStrongestNeed().tryCreateTask();
        }
        return needTask;
    }

    /**
     * Calls TaskContainer to find appropriate task for this actor and his position
     *
     * @return
     */
    private Task getTaskFromContainer() {
        return gameContainer.getTaskContainer().getActiveTask(aspectHolder.getPosition());
    }

    /**
     * Sets this actor as performer to taken task.
     */
    private void claimTask() {
        currentTask.setPerformer((Unit) aspectHolder);
    }

    /**
     * Reverts state of task as it is newly created.
     */
    public void freeTask() {
        if (currentTask != null) {
            currentTask.reset();
            currentTask = null;
        }
        movementNeeded = false;
    }

    public boolean isTargetExact() {
        if (currentTask != null) {
            return currentTask.getNextAction().getTargetAspect().isExactTarget();
        }
        return false;
    }

    public Position getTarget() {
        return currentTask != null && currentTask.getNextAction() != null ?
                currentTask.getNextAction().getTargetPosition() :
                null;
    }

    public boolean isMovementNeeded() {
        return movementNeeded;
    }
}