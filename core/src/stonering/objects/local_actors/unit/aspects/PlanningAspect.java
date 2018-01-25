package stonering.objects.local_actors.unit.aspects;

import stonering.objects.jobs.actions.Action;
import stonering.objects.jobs.Task;
import stonering.objects.local_actors.Aspect;
import stonering.objects.local_actors.unit.Unit;
import stonering.global.utils.Position;

/**
 * Created by Alexander on 10.10.2017.
 * <p>
 * Holds current creature's task and it's steps. resolves behavior, if some step fails.
 */
public class PlanningAspect extends Aspect {
    private Task currentTask;
    private Action currentAction;
    private Position target;

    public PlanningAspect(Unit unit) {
        this.aspectHolder = unit;
        this.name = "planning";
    }

    public void turn() {
        if (checkActions()) { // has active action
            if (checkUnitPosition()) {
                currentAction.perform(); // act
                if (currentAction.isFinished()) {
                    System.out.println("task finished");
                }
            }
        } else {
            repairActions();
        }
    }

    // action exists and not finished
    private boolean checkActions() {
        return currentTask != null && currentAction != null &&
                !currentTask.isFinished() && !currentAction.isFinished();
    }

    // if aspectHolder in position for acting
    private boolean checkUnitPosition() {
        return aspectHolder.getPosition().equals(target);
    }

    private void repairActions() {
        if (currentTask == null || currentTask.isFinished()) {
            if (getTaskFromContainer()) {
                currentAction = currentTask.getNextAction();
                updateTarget();
            }
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
            currentAction = currentTask.getNextAction();
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
        if (currentAction.isTargetExact()) {
            target = currentAction.getTargetPosition();
        } else {
            target = getNextToTargetPosition();
        }
    }

    private Position getNextToTargetPosition() {
        Position target = currentAction.getTargetPosition();
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                if (gameContainer.getLocalMap().isWalkPassable(target.getX() + x, target.getY() + y, target.getZ()))
                    return new Position(target.getX() + x, target.getY() + y, target.getZ());
            }
        }
        return null;
    }
}