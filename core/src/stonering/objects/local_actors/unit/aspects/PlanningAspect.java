package stonering.objects.local_actors.unit.aspects;

import stonering.objects.jobs.Task;
import stonering.objects.jobs.actions.Action;
import stonering.objects.local_actors.Aspect;
import stonering.objects.local_actors.AspectHolder;
import stonering.global.utils.Position;

/**
 * Created by Alexander on 10.10.2017.
 * <p>
 * Holds current creature's task and it's steps. resolves behavior, if some step fails.
 */
public class PlanningAspect extends Aspect {
    private Task currentTask;
    //    private Action currentAction;
    private Position target;

    public PlanningAspect(AspectHolder aspectHolder) {
        super("planning", aspectHolder);
    }

    public void turn() {
        if (checkTask()) { // has active action
            if (currentTask.getNextAction().getRequirementsAspect().check()) { //check action requirements
                if (checkUnitPosition()) {
                    currentTask.getNextAction().perform(); // act
                }
            }
        } else {
            repairActions();
        }
    }

    // action exists and not finished
    private boolean checkTask() {
        return currentTask != null && !currentTask.isFinished();
    }

    // if aspectHolder in position for acting
    private boolean checkUnitPosition() {
        return aspectHolder.getPosition().equals(target);
    }

    private void repairActions() {
        if (getTaskFromContainer()) {
            updateTarget();
        }
    }

    private boolean getTaskFromContainer() {
        Task task = gameContainer.getTaskContainer().getActiveTask();
        if (task != null) {
            currentTask = task;
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

    // returns target for moving, used by MovementAspect
    public Position getTarget() {
        return target;
    }

    private void updateTarget() {
        Action action = currentTask.getNextAction();
        target = action.getTargetPosition();
        if (!action.isTargetExact()) {
            for (int x = -1; x < 2; x++) {
                for (int y = -1; y < 2; y++) {
                    if (gameContainer.getLocalMap().isWalkPassable(target.getX() + x, target.getY() + y, target.getZ()))
                        target = new Position(target.getX() + x, target.getY() + y, target.getZ());
                }
            }
            target = null;
        }
    }
}