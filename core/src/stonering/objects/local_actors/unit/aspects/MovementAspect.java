package stonering.objects.local_actors.unit.aspects;

import stonering.game.core.model.GameContainer;
import stonering.game.core.model.LocalMap;
import stonering.global.utils.pathfinding.NoPathException;
import stonering.global.utils.pathfinding.a_star.AStar;
import stonering.objects.local_actors.Aspect;
import stonering.objects.local_actors.unit.Unit;
import stonering.global.utils.Position;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Alexander on 06.10.2017.
 * <p>
 * Holds movement speed, current path, movement status. also builds path.
 */
public class MovementAspect extends Aspect {
    private int stepTime;
    private int stepDelay;

    private LocalMap map;

    private PlanningAspect planning;

    private ArrayList<Position> path;
    private boolean oldTarget;

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
        if (path != null) {
            if (!path.isEmpty()) {// path not finished
                Position nextPosition = path.remove(0); // get next step, remove from path
                if (map.isWalkPassable(nextPosition)) { // path has not been blocked after calculation
                    aspectHolder.setPosition(nextPosition); //step
                } else { // path blocked
                    System.out.println("path blocked");
                    path = null; // drop path
                }
            } else {
                path = null; // drop path
            }
        } else {
            if (planning != null
                    && planning.getTarget() != null
                    && !planning.getTarget().equals(aspectHolder.getPosition())) {
                makeRouteToTarget();
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
        path = new AStar(gameContainer.getLocalMap()).makeShortestPath(aspectHolder.getPosition(), planning.getTarget());
    }
}