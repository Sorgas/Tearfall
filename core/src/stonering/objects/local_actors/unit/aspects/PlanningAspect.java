package stonering.objects.local_actors.unit.aspects;

import stonering.objects.jobs.Task;
import stonering.objects.jobs.actions.Action;
import stonering.objects.local_actors.Aspect;
import stonering.objects.local_actors.AspectHolder;
import stonering.global.utils.Position;
import stonering.objects.local_actors.unit.Unit;

/**
 * @author Alexander Kuzyakov on 10.10.2017.
 *         <p>
 *         Holds current creature's task and it's steps. resolves behavior, if some step fails.
 *         Updates target for movement on task switching.
 */
public class PlanningAspect extends Aspect {
    private Task currentTask;

    public PlanningAspect(AspectHolder aspectHolder) {
        super("planning", aspectHolder);
    }

    /**
     * Called from {@link AspectHolder}
     */
    public void turn() {
        if (checkTask()) {
            if (checkUnitPosition()) { // actor on position
                if (checkActionSequence()) {
                    if (currentTask.getNextAction().perform()) { // act. called several times
                        System.out.println("action completed");
                    }
                }
            } // keep moving to target
        } else {
            selectTask();// try find task, check it and claim
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
     * Checks if actor is in appropriate position for acting.
     * Modifies target if it is blocked by actor.
     *
     * @return
     */
    private boolean checkUnitPosition() {
        Position pos = aspectHolder.getPosition();
        if (currentTask.getNextAction().isTargetExact()) {
            if (currentTask.getNextAction().isTargetNear()) {
                return getTarget().getDistanse(aspectHolder.getPosition()) < 2; // exact and near
            } else {
                return pos.equals(getTarget()); // exact only
            }
        } else {
            if (currentTask.getNextAction().isTargetNear()) {
                if (pos.equals(getTarget())) {
                    currentTask.getNextAction().getTargetAspect().createActionToStepOff(); // make 1 step away
                    return false;
                } else {
                    return aspectHolder.getPosition().isNeighbor(getTarget()); // near only
                }
            }
            System.out.println("WARN: action " + currentTask.getNextAction() + " target not defined as exact or near");
            return getTarget().getDistanse(aspectHolder.getPosition()) < 2; // not valid
        }
    }

    /**
     * Finds appropriate task for this performer.
     * Currently gets Task from container;
     * TODO needs tasks
     * TODO combat tasks
     */
    private void selectTask() {
        ((NeedsAspect) aspectHolder.getAspects().get("needs")).checkNeeds();
        if (getTaskFromContainer()) {
            claimTask();
            if (checkActionSequence()) { //checking requires performer to be set.
                // ok
            } else {
                freeTask();
            }
        }
    }

    /**
     * Calls TaskContainer to find appropriate task for this actor and his position
     *
     * @return
     */
    private boolean getTaskFromContainer() {
        Task task = gameContainer.getTaskContainer().getActiveTask(aspectHolder.getPosition());
        if (task != null) {
            currentTask = task;
            return true;
        } else {
            return false;
        }
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
        currentTask.reset();
        currentTask = null;
    }

    public boolean isTargetExact() {
        if (currentTask != null) {
            return currentTask.getNextAction().getTargetAspect().isExactTarget();
        }
        return false;
    }

    public Position getTarget() {
        return currentTask != null ? currentTask.getNextAction().getTargetPosition() : null;
    }
}