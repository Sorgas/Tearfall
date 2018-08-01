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
    private Position target;
    private boolean performingStarted = false;

    public PlanningAspect(AspectHolder aspectHolder) {
        super("planning", aspectHolder);
    }

    public void turn() {
        if (checkTask()) { // has active action
            if (target != null) { // has current action
                if (checkUnitPosition()) { // actor on position
//                    if (performingStarted) { // in the middle of performing
                    if (currentTask.getNextAction().perform()) { // act. called several times
                        System.out.println("action completed");
                        target = null;
                        performingStarted = false;
                    }
//                    } else { // starting performing
//                        Action currentAction = currentTask.getNextAction();
//                        if (checkActionSequence()) { //check action requirements again
//                            if(currentAction == currentTask.getNextAction()) {
//                                System.out.println("action checked before acting: OK");
//                                performingStarted = true;
//                            } else {
//                                updateTarget(); //set target
//                            }
//                        } else {
//                            System.out.println("action checked before acting: FAIL");
//                            freeTask();
//                        }
//                    }
                }// keep moving to target
            } else { // has no current action
                if (checkActionSequence()) { //check action requirements
                    System.out.println("action checked on assign: OK");
                    updateTarget(); //set target
                } else {
                    System.out.println("action checked on assign: FAIL");
                    freeTask();
                }
            }
        } else {
            repairTask();// try find task
        }
    }

    private boolean checkActionSequence() {
        Action currentAction = null;
        boolean lastCheck = false;
        do {
            currentAction = currentTask.getNextAction();
            lastCheck = currentAction.getRequirementsAspect().check();
        } while (currentAction != currentTask.getNextAction()); //stops
        return lastCheck;
    }

    // action exists and not finished
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
                return target.getDistanse(aspectHolder.getPosition()) < 2;
            } else {
                return pos.equals(target);
            }
        } else {
            if (currentTask.getNextAction().isTargetNear()) {
                if (pos.equals(target)) { // need modify target to free it`s cell
                    currentTask.getNextAction().getTargetAspect().createActionToStepOff();
                    return false;
                } else {
                    System.out.println("action target is blocked");
                    freeTask();
                    return false;
                }
            }
            return target.getDistanse(aspectHolder.getPosition()) < 2;
        }
    }

    private void repairTask() {
        getTaskFromContainer();
    }

    private boolean getTaskFromContainer() {
        Task task = gameContainer.getTaskContainer().getActiveTask(aspectHolder.getPosition());
        if (task != null) {
            claimTask(task);
            return true;
        } else {
            // create task by needs
        }
        return false;
    }

    //should contain task selecting logic
    private boolean fetchTasks() {
        if (!gameContainer.getTaskContainer().getTasks().isEmpty()) {
            currentTask = gameContainer.getTaskContainer().getTasks().get(0);
            return true;
        } else {
            // create task with creature needs
        }
        return false;
    }

    private void claimTask(Task task) {
        currentTask = task;
        task.setPerformer((Unit) aspectHolder);
    }

    public void freeTask() {
        currentTask.reset();
        currentTask = null;
        performingStarted = false;
    }

    // returns target for moving, used by MovementAspect
    public Position getTarget() {
        return target;
    }

    private void updateTarget() {
        Action action = currentTask.getNextAction();
        target = action.getTargetPosition();
    }

    public boolean isTargetExact() {
        if (currentTask != null) {
            return currentTask.getNextAction().getTargetAspect().isExactTarget();
        }
        return false;
    }
}