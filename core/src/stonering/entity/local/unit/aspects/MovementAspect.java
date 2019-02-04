package stonering.entity.local.unit.aspects;

import stonering.game.core.model.local_map.LocalMap;
import stonering.util.pathfinding.a_star.AStar;
import stonering.entity.local.Aspect;
import stonering.entity.local.unit.Unit;
import stonering.util.geometry.Position;
import stonering.util.global.TagLoggersEnum;

import java.util.List;

/**
 * Holds movement speed, current path, movement status. also builds path.
 *
 * @author Alexander Kuzyakov on 06.10.2017.
 */
public class MovementAspect extends Aspect {
    public static String NAME = "movement";
    private LocalMap localMap;
    private PlanningAspect planning;
    private Position cachedTarget;
    private List<Position> cachedPath;

    private int stepDelay;
    private int stepTime;

    public MovementAspect(Unit unit) {
        super(unit);
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
                    if (localMap.isWalkPassable(nextPosition)) { // path has not been blocked after calculation
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
                        planning.reset();
                    }
                }
            }
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void init() {
        super.init();
        if (aspectHolder.getAspects().containsKey("planning"))
            planning = (PlanningAspect) aspectHolder.getAspects().get("planning");
        localMap = gameContainer.get(LocalMap.class);
    }

    private void makeRouteToTarget() {
        cachedPath = new AStar(gameContainer.get(LocalMap.class)).makeShortestPath(aspectHolder.getPosition(), planning.getTarget(), planning.isTargetExact());
    }

    /**
     * Moves creature lower, if it is above ground.
     * Also deletes it's path, as target may be inaccessible after fall.
     * //TODO apply fall damage
     */
    private void tryFall() {
        Position pos = aspectHolder.getPosition();
        if (localMap.isFlyPassable(pos) &&
                !localMap.isWalkPassable(pos) &&
                pos.getZ() > 0 && localMap.isFlyPassable(pos.getX(), pos.getY(), pos.getZ() - 1)) {
            aspectHolder.setPosition(new Position(pos.getX(), pos.getY(), pos.getZ() - 1));
            cachedPath = null;
        }
    }
}