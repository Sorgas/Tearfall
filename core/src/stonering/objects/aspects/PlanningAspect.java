package stonering.objects.aspects;

import stonering.game.core.model.GameContainer;
import stonering.objects.common.Path;
import stonering.objects.jobs.Action;
import stonering.objects.jobs.Task;
import stonering.objects.local_actors.unit.Unit;
import stonering.global.utils.Position;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Alexander on 10.10.2017.
 * <p>
 * Holds current creature's task and it's steps. resolves behavior, if some step fails.
 */
public class PlanningAspect extends Aspect {
    private GameContainer gameContainer;
    private ArrayList<Position> route;
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
        if(currentTask == null) {
            fetchTasks();
        }
        if(currentTask != null) {
            currentAction = currentTask.getNextAction();
            if(!unit.getPosition().equals(currentAction.getTargetPosition())) {

            }
        }
    }

//    private Path getPathToTarget(Position target) {
//
//    }

    public Position getStep() {
//        if(route.size() > 0) {
//        return route.get(0);}else {
//            return unit.getPosition();
//        }
        Random random = new Random();
        int dx = random.nextInt(3) - 1;
        int dy = random.nextInt(3) - 1;
        Position current = unit.getPosition();
        Position newPosition = new Position(current.getX() + dx, current.getY() + dy, current.getZ());
        if (newPosition.getX() < 0 || newPosition.getX() > 191) newPosition.setX(current.getX());
        if (newPosition.getY() < 0 || newPosition.getY() > 191) newPosition.setY(current.getY());
        return newPosition;
    }

    private void fetchTasks() {
        if (!gameContainer.getTaskContainer().getTasks().isEmpty())
            currentTask = gameContainer.getTaskContainer().getTasks().get(0);
    }

    public void poll() {
//        if (route.size() > 0) route.remove(0);
    }

}
