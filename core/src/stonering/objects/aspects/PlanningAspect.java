package stonering.objects.aspects;

import stonering.game.core.model.GameContainer;
import stonering.global.utils.pathfinding.NoPathException;
import stonering.global.utils.pathfinding.a_star.AStar;
import stonering.objects.common.Path;
import stonering.objects.jobs.actions.Action;
import stonering.objects.jobs.Task;
import stonering.objects.jobs.actions.ActionTypeEnum;
import stonering.objects.local_actors.unit.Unit;
import stonering.global.utils.Position;

/**
 * Created by Alexander on 10.10.2017.
 * <p>
 * Holds current creature's task and it's steps. resolves behavior, if some step fails.
 */
public class PlanningAspect extends Aspect {
    private GameContainer gameContainer;
    private Path route;
    private Task currentTask;
    private Action currentAction;

    public PlanningAspect(Unit unit) {
        this.unit = unit;
        this.name = "planning";
    }

    @Override
    public void init(GameContainer gameContainer) {
        this.gameContainer = gameContainer;
    }

    public void turn() {
        if (currentAction == null)
            fetchTasks();
        if (route == null)
            makeRouteToTarget();
        if(route.isFinished()) {
            performAction();
        } else {
            ((MovementAspect) unit.getAspects().get("movement")).move();
        }
    }

    private void performAction() {
//        if(currentAction.getActionType() == ActionTypeEnum.)
//        ((ActionAspect) unit.getAspects().get("action")).performAction(currentAction);
    }

    public Position getStep() {
        if (!route.isFinished()) {
            return route.pollNextPosition();
        } else {
            return unit.getPosition();
        }
    }

    private void makeRouteToTarget() {
        try {
            route = new AStar(unit.getLocalMap()).findPath(unit.getPosition(), currentAction.getTargetPosition());
        } catch (NoPathException e) {
            System.out.println("cancel task");
        }
    }

    //should contain task selecting logic
    private void fetchTasks() {
        if (!gameContainer.getTaskContainer().getTasks().isEmpty()) {
            currentTask = gameContainer.getTaskContainer().getTasks().get(0);
            currentAction = currentTask.getNextAction();
        }
    }

    private void analizeAction() {

    }

    public void dropRoute() {
        route = null;
    }
}