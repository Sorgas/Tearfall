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
    private Path route;
    private Task currentTask;
    private Action currentAction;

    public PlanningAspect(Unit unit) {
        this.unit = unit;
        this.name = "planning";
    }

    public void turn() {
        if (checkActions()) {
            if (route == null)
                makeRouteToTarget();
            if (route.isFinished()) {
                performAction();
            }
        } else {
            repairActions();
        }
    }

    private boolean checkActions() {
        return currentTask != null && currentAction != null && route != null;
    }

    private void repairActions() {
        if (currentTask == null) {
            fetchTasks();
        } else {
            if(currentAction.isFinished() || currentAction == null) {
                currentAction = currentTask.getNextAction();
            }
        }
    }

    private void performAction() {
        currentAction.perform();
    }

    public Position getStep() {
        if (route != null && !route.isFinished()) {
            return route.pollNextPosition();
        } else {
            return unit.getPosition();
        }
    }

    private void makeRouteToTarget() {
        try {
            route = new AStar(gameContainer.getLocalMap()).findPath(unit.getPosition(), currentAction.getTargetPosition());
        } catch (NoPathException e) {
            System.out.println("cancel task");
        }
    }

    private void checkActionsStatus() {

    }

    //should contain task selecting logic
    private void fetchTasks() {
        if (!gameContainer.getTaskContainer().getTasks().isEmpty()) {
            currentTask = gameContainer.getTaskContainer().getTasks().get(0);
            currentAction = currentTask.getNextAction();
        } else {
            // create task with creature needs
        }
    }

    private void analizeAction() {

    }

    public void dropRoute() {
        route = null;
    }
}