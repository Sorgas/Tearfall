package stonering.objects.local_actors.unit.aspects;

import stonering.global.utils.pathfinding.NoPathException;
import stonering.global.utils.pathfinding.a_star.AStar;
import stonering.objects.common.Path;
import stonering.objects.jobs.actions.Action;
import stonering.objects.jobs.Task;
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

    public PlanningAspect(Unit unit) {
        this.unit = unit;
        this.name = "planning";
    }

    public void turn() {
        if (checkActions()) { // has active action
            if (checkUnitPosition()) {
                currentAction.perform(); // act
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

    // if unit in position for acting
    private boolean checkUnitPosition() {
        return unit.getPosition().equals(currentAction.getTargetPosition());
    }

    private void repairActions() {
        if (currentTask == null || currentTask.isFinished()) {
            if(getTaskFromContainer()) {
                currentAction = currentTask.getNextAction();
            }
        }
    }

    private boolean getTaskFromContainer() {
        if (!gameContainer.getTaskContainer().getTasks().isEmpty()) {
            currentTask = gameContainer.getTaskContainer().getTasks().get(0);
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
        if (currentAction != null && !currentAction.isFinished()) {
            return currentAction.getTargetPosition();
        } else {
            return null;
        }
    }
}