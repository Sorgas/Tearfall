package stonering.entity.unit.aspects;

import com.badlogic.gdx.math.Vector3;
import stonering.entity.PositionAspect;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.model.system.units.UnitContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.util.global.Initable;
import stonering.util.pathfinding.a_star.AStar;
import stonering.entity.Aspect;
import stonering.entity.unit.Unit;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

import java.util.List;

/**
 * Holds movement speed, current path, movement status. also builds path.
 *
 * @author Alexander Kuzyakov on 06.10.2017.
 */
public class MovementAspect extends Aspect implements Initable {
    public static String NAME = "movement";
    private LocalMap localMap;
    private UnitContainer unitContainer;
    private AStar aStar;
    private PlanningAspect planning;
    private AttributeAspect attribute;
    private Position target;
    private List<Position> path;
    private float stepProgress;
    private int stepInterval;

    public MovementAspect(Unit unit) {
        super(unit);
        this.entity = unit;
        stepInterval = 15;
        stepProgress = 0;
    }

    @Override
    public void init() {
        GameModel model = GameMvc.instance().getModel();
        localMap = model.get(LocalMap.class);
        unitContainer = model.get(UnitContainer.class);
        aStar = model.get(AStar.class);
        planning = entity.getAspect(PlanningAspect.class);
        attribute = entity.getAspect(AttributeAspect.class);
    }

    public void turn() {
        if (tryFall()) return; // if creature is not on the passable tile, it falls.
        if (stepInProgeress()) return;
        update();
        if (hasPath()) makeStep();
    }

    /**
     * Moves creature lower, if it is above ground.
     * Deletes target and path, for recalculation on next iteration.
     * //TODO apply fall damage
     */
    private boolean tryFall() {
        Position pos = getPosition();
        if (!canFall()) return false;
        unitContainer.updateUnitPosiiton((Unit) entity, new Position(pos.x, pos.y, pos.z - 1));
        target = null;
        path = null;
        return true;
    }

    /**
     * Creature can fall, if is in space cell, and cell below is not wall.
     */
    private boolean canFall() {
        Position pos = getPosition();
        return !localMap.isWalkPassable(pos) &&
                localMap.isFlyPassable(pos) &&
                pos.z > 0 &&
                localMap.isFlyPassable(pos.x, pos.y, pos.z - 1);
    }

    /**
     * Counts ticks to the next step;
     */
    private boolean stepInProgeress() {
        return (++stepProgress < stepInterval);
    }

    /**
     * Update state of this aspect, according target from {@link PlanningAspect}.
     */
    private void update() {
        stepProgress = 0;
        if (target == planning.getTarget()) return; // target is old
        target = planning.getTarget();
        if (updatePath()) return; // path successfully found or not needed
        target = null;
        path = null;
        planning.interrupt();
    }

    /**
     * Moves creature to the next tile from path. path should not be null.
     */
    private void makeStep() {
        Position nextPosition = path.remove(0); // get next step, remove from path
        if (localMap.isWalkPassable(nextPosition)) { // path has not been blocked after calculation
            unitContainer.updateUnitPosiiton((Unit) entity, nextPosition); //step
        } else { // path blocked
            Logger.PATH.log("path to " + target + " was blocked in " + nextPosition);
            target = null; // drop path, will be recounted on next step.
            path = null;
        }
    }

    /**
     * Updates path according to target. For null target path is set to null;
     *
     * @return false, if no path found for non-null target.
     */
    private boolean updatePath() {
        path = target != null ? aStar.makeShortestPath(getPosition(), target, planning.isTargetExact()) : null;
        return (path == null) == (target == null); // target and path should be both either set or null.
    }

    /**
     * Checks that this aspect holder has poth to move on.
     */
    private boolean hasPath() {
        return path != null && !path.isEmpty();
    }

    /**
     * Returns vector with [0:1] floats, representing current progress of movement.
     */
    public Vector3 getStepProgressVector() {
        if (!hasPath()) return new Vector3(); // zero vector for staying still.
        Position nextPosition = path.get(0);
        Position unitPosition = entity.getAspect(PositionAspect.class).position;
        return new Vector3(
                getStepProgressVectorComponent(unitPosition.x, nextPosition.x),
                getStepProgressVectorComponent(unitPosition.y, nextPosition.y),
                getStepProgressVectorComponent(unitPosition.z, nextPosition.z));
    }

    private float getStepProgressVectorComponent(int from, int to) {
        return (to - from) * stepProgress / stepInterval;
    }

    private Position getPosition() {
        return entity.getAspect(PositionAspect.class).position;
    }
}