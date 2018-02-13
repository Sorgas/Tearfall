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
    private Position target;

    public MovementAspect(Unit unit) {
        super("movement", unit);
        this.aspectHolder = unit;
        stepTime = 15;
        stepDelay = new Random().nextInt(stepTime);
    }

    public void turn() {
        if (stepDelay > 0) {
            stepDelay--; //counting ticks to step
        } else {
            makeStep();
        }
    }

    private void makeStep() {
        if (planning != null) {
            if (isTargetOld()) {// old target
                if (path == null)
                    makeRouteToTarget();
                else {
                    if (!path.isEmpty()) { // path not finished
                        Position nextPosition = path.remove(0); // get next step, remove from path
                        if (map.isWalkPassable(nextPosition)) { // path has not been blocked after calculation
                            aspectHolder.setPosition(nextPosition); //step
                        } else { // path blocked
                            makeRouteToTarget(); // calculate new path
                        }
                    }
                }
            } else { //new target
                target = planning.getTarget();
                if (target != null) {
                    makeRouteToTarget();
                }
            }
            stepDelay = stepTime;
        } else {
            //no planning aspect
        }
    }

    private boolean isTargetOld() {
        Position target = planning.getTarget();
        if (target != null) {
            return target.equals(this.target);
        }
        return false;
    }

    @Override
    public void init(GameContainer gameContainer) {
        super.init(gameContainer);
        if (aspectHolder.getAspects().containsKey("planning"))
            planning = (PlanningAspect) aspectHolder.getAspects().get("planning");
        map = gameContainer.getLocalMap();
    }

    private void makeRouteToTarget() {
        System.out.println("path start");
        path = new AStar(gameContainer.getLocalMap()).makeShortestPath(aspectHolder.getPosition(), planning.getTarget());
        System.out.println("path end");
    }
}