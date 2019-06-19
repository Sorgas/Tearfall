package stonering.entity.local.unit.aspects;

import com.badlogic.gdx.math.Vector3;
import stonering.entity.local.PositionAspect;
import stonering.game.GameMvc;
import stonering.game.model.lists.UnitContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.util.pathfinding.a_star.AStar;
import stonering.entity.local.Aspect;
import stonering.entity.local.unit.Unit;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

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

    private float stepProgress;
    private int stepInterval;

    public MovementAspect(Unit unit) {
        super(unit);
        this.entity = unit;
        stepInterval = 15;
        stepProgress = stepInterval;
    }

    public void turn() {
        if (tryFall()) return;
        if (stepProgress > 0) {
            stepProgress--; // counting ticks to step.
        } else {
            makeStep();
            stepProgress = stepInterval;
        }
    }

    private void makeStep() {
        if (!planning.isMovementNeeded()) return;
        if (cachedTarget != null && cachedTarget.equals(planning.getTarget())) { //old target
            if (hasPath()) {
                Position nextPosition = cachedPath.remove(0); // get next step, remove from path
                if (localMap.isWalkPassable(nextPosition)) { // path has not been blocked after calculation
                    gameMvc.getModel().get(UnitContainer.class).updateUnitPosiiton((Unit) entity, nextPosition); //step
                } else { // path blocked
                    Logger.PATH.log("path to " + cachedTarget + " was blocked in " + nextPosition);
                    cachedTarget = null; // drop path
                }
            }
            // path finished, stay
        } else { // new target
            cachedTarget = planning.getTarget();
            if (cachedTarget != null) {
                makeRouteToTarget();
                if (cachedPath == null) { // no path found, fail task
                    cachedTarget = null;
                    planning.reset();
                }
            }
        }
    }

    @Override
    public void init() {
        super.init();
        planning = entity.getAspect(PlanningAspect.class);
        localMap = GameMvc.instance().getModel().get(LocalMap.class);
    }

    private void makeRouteToTarget() {
        cachedPath = new AStar(GameMvc.instance().getModel().get(LocalMap.class)).makeShortestPath(getPosition(), planning.getTarget(), planning.isTargetExact());
    }

    /**
     * Moves creature lower, if it is above ground.
     * Also deletes it's path, as target may be inaccessible after fall.
     * //TODO apply fall damage
     */
    private boolean tryFall() {
        Position pos = getPosition();
        if (localMap.isFlyPassable(pos) &&
                !localMap.isWalkPassable(pos) &&
                pos.getZ() > 0 && localMap.isFlyPassable(pos.getX(), pos.getY(), pos.getZ() - 1)) {
            entity.getAspect(PositionAspect.class).position = new Position(pos.getX(), pos.getY(), pos.getZ() - 1);
            cachedPath = null;
            return true;
        }
        return false;
    }

    /**
     * Checks that this aspect holder has poth to move on.
     *
     * @return
     */
    private boolean hasPath() {
        return cachedPath != null && !cachedPath.isEmpty();
    }

    /**
     * Returns vector with [0:1] floats, representing current progress of movement.
     */
    public Vector3 getStepProgressVector() {
        if (!hasPath()) return new Vector3(); // zero vector for staying still.
        Position nextPosition = cachedPath.get(0);
        Position unitPosition = entity.getAspect(PositionAspect.class).position;
        return new Vector3(
                getStepProgressVectorComponent(unitPosition.x, nextPosition.x),
                getStepProgressVectorComponent(unitPosition.y, nextPosition.y),
                getStepProgressVectorComponent(unitPosition.z, nextPosition.z));
    }

    private float getStepProgressVectorComponent(int from, int to) {
        return (from - to) * stepProgress / stepInterval;
    }

    private Position getPosition() {
        return entity.getAspect(PositionAspect.class).position;
    }
}