package stonering.entity.local.unit.aspects;

import stonering.game.core.model.GameContainer;
import stonering.game.core.model.LocalMap;
import stonering.global.utils.pathfinding.a_star.AStar;
import stonering.entity.local.Aspect;
import stonering.entity.local.unit.Unit;
import stonering.global.utils.Position;
import stonering.utils.global.TagLoggersEnum;

import java.util.List;

/**
 * Holds movement speed, current path, movement status. also builds path.
 *
 * @author Alexander Kuzyakov on 06.10.2017.
 */
public class MovementAspect extends Aspect {
    private int stepTime;
    private int stepDelay;
    private LocalMap map;
    private PlanningAspect planning;
    private Position cachedTarget;
    private List<Position> cachedPath;

    public MovementAspect(Unit unit) {
        super("movement", unit);
        this.aspectHolder = unit;
        stepTime = 6;
        stepDelay = stepTime;
    }

    public void turn() {
        tryFall();
        if (stepDelay > 0) {
            stepDelay--; //counting ticks to step
        } else {
            makeStep();
            stepDelay = stepTime;
        }
    }

    private void makeStep() {
        if (planning.isMovementNeeded()) {
            if (cachedTarget != null && cachedTarget.equals(planning.getTarget())) { //old target
                if (cachedPath != null && !cachedPath.isEmpty()) {// path not finished
                    Position nextPosition = cachedPath.remove(0); // get next step, remove from path
                    if (map.isWalkPassable(nextPosition)) { // path has not been blocked after calculation
                        aspectHolder.setPosition(nextPosition); //step
                    } else { // path blocked
                        TagLoggersEnum.PATH.log("path to " + cachedTarget + " was blocked in " + nextPosition);
                        cachedTarget = null; // drop path
                    }
                }
                // path finished, stay
            } else { // new target
                cachedTarget = planning.getTarget();
                if (cachedTarget != null) {
                    makeRouteToTarget();
                    if (cachedPath == null) { // no path found, fail task
                        planning.freeTask();
                    }
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
        cachedPath = new AStar(gameContainer.getLocalMap()).makeShortestPath(aspectHolder.getPosition(), planning.getTarget(), planning.isTargetExact());
    }

    /**
     * Moves creature lower, if it is above ground.
     * Also deletes it's path, as target may be inaccessible after fall.
     * //TODO apply fall damage
     */
    private void tryFall() {
        Position pos = aspectHolder.getPosition();
        if (map.isFlyPassable(pos) &&
                !map.isWalkPassable(pos) &&
                pos.getZ() > 0 && map.isFlyPassable(pos.getX(), pos.getY(), pos.getZ() - 1)) {
            aspectHolder.setPosition(new Position(pos.getX(), pos.getY(), pos.getZ() - 1));
            cachedPath = null;
        }
    }
}