package stonering.objects.local_actors.unit.aspects;

import stonering.game.core.model.GameContainer;
import stonering.game.core.model.LocalMap;
import stonering.global.utils.pathfinding.a_star.AStar;
import stonering.objects.local_actors.Aspect;
import stonering.objects.local_actors.unit.Unit;
import stonering.global.utils.Position;

import java.util.List;

/**
 * @author Alexander Kuzyakov on 06.10.2017.
 * <p>
 * Holds movement speed, current path, movement status. also builds path.
 */
public class MovementAspect extends Aspect {
    private int stepTime;
    private int stepDelay;
    private LocalMap map;
    private PlanningAspect planning;
    private List<Position> path;
    private Position cachedTarget;

    public MovementAspect(Unit unit) {
        super("movement", unit);
        this.aspectHolder = unit;
        stepTime = 15;
        stepDelay = stepTime;
    }

    public void turn() {
        if (stepDelay > 0) {
            stepDelay--; //counting ticks to step
        } else {
            makeStep();
            stepDelay = stepTime;
        }
    }

    private void makeStep() {
        if (cachedTarget != null && cachedTarget.equals(planning.getTarget())) { //old target
            if (path != null && !path.isEmpty()) {// path not finished
                Position nextPosition = path.remove(0); // get next step, remove from path
                if (map.isWalkPassable(nextPosition)) { // path has not been blocked after calculation
                    aspectHolder.setPosition(nextPosition); //step
                } else { // path blocked
                    System.out.println("path blocked");
                    cachedTarget = null; // drop path
                }
            }
            // path finished, stay
        } else { // new target
            cachedTarget = planning.getTarget();
            if (cachedTarget != null) {
                makeRouteToTarget();
                if (path == null) { // no path found, fail task
                    planning.freeTask();
                }
            }
        }
    }

    @Override
    public void init(GameContainer gameContainer) {
        super.init(gameContainer);
        if (aspectHolder.getAspects().containsKey("planning"))
            planning = (PlanningAspect) aspectHolder.getAspects().get("planning");
        map = gameContainer.getLocalMap();
    }

    private void makeRouteToTarget() {
        path = new AStar(gameContainer.getLocalMap()).makeShortestPath(aspectHolder.getPosition(), planning.getTarget(), planning.isTargetExact());
    }
}