package stonering.game.model.system.units;

import com.badlogic.gdx.math.Vector3;
import stonering.entity.FloatPositionEntity;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.MovementAspect;
import stonering.entity.unit.aspects.PlanningAspect;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;
import stonering.util.pathfinding.a_star.AStar;

/**
 * Changes {@link Unit}'s position over time, if one has target to move.
 * Works with vector position of a unit. Unit's integer position is updated accordingly, see{@link FloatPositionEntity}.
 *
 * @author Alexander on 21.10.2019.
 */
public class CreatureMovementSystem {
    private LocalMap localMap;
    private UnitContainer unitContainer;
    private AStar aStar;

    public void updateUnitPosition(Unit unit) {
        GameModel model = GameMvc.instance().getModel();
        localMap = model.get(LocalMap.class);
        unitContainer = model.get(UnitContainer.class);
        aStar = model.get(AStar.class);
        if (tryFall(unit)) return; // if creature is not on the passable tile, it falls and looses its task.
        if (checkPath()) makeStep(unit, unit.getAspect(MovementAspect.class));
    }

    /**
     * Moves creature lower, if it is above ground.
     * Deletes target and path, for recalculation on next iteration.
     * //TODO apply fall damage
     */
    private boolean tryFall(Unit unit) {
        if (!canFall(unit)) return false;
        Position pos = unit.position;
        unitContainer.updateUnitPosiiton(unit, new Position(pos.x, pos.y, pos.z - 1));
        unit.getAspect(PlanningAspect.class).interrupt();
        unit.getAspect(MovementAspect.class).path = null;
        return true;
    }

    /**
     * Creature can fall, if is in space cell, and cell below is fly passable.
     */
    private boolean canFall(Unit unit) {
        Position pos = unit.position;
        return localMap.getBlockTypeEnumValue(pos) == BlockTypesEnum.SPACE && // can fall through SPACE
                pos.z > 0 && // not the bottom of a map
                localMap.isFlyPassable(pos.x, pos.y, pos.z - 1); // lower tile is open
    }

    /**
     * Update state of this aspect, according target from {@link PlanningAspect}.
     */
    private void update(Unit unit) {
        stepProgress = 0;
        if (target == planning.getTarget()) return; // target is old
        target = planning.getTarget();
        if (updatePath()) return; // path successfully found or not needed
        target = null;
        path = null;
        planning.interrupt();
    }

    /**
     * Moves creature to the next tile from path.
     */
    private void makeStep(Unit unit, MovementAspect aspect) {
        Position nextPosition = aspect.path.get(0);
        if (localMap.isWalkPassable(nextPosition)) { // path has not been blocked after calculation
            Vector3 movementVector = getDirectionVector(unit.position, nextPosition).scl(aspect.speed);  // movement on this update
            Vector3 newVectorPosition = unit.vectorPosition.cpy().add(movementVector);
            unitContainer.updateUnitPosiiton(unit, newVectorPosition); // change unit position in container
            if (unit.position.equals(nextPosition)) { // tile changed
                aspect.path.remove(0); // remove reached tile from path
                unitContainer.healthSystem.applyMoveChange(unit);
            }
        } else { // path blocked
            Logger.PATH.log("path was blocked in " + nextPosition);
            aspect.path = null; // drop path, will be recounted on next step
        }
    }

    /**
     * Updates path according to target. For null target path is set to null;
     *
     * @return false, if no path found for non-null target.
     */
    private boolean updatePath() {

    }

    /**
     * Checks that this aspect holder has poth to move on.
     */
    private boolean checkPath(Unit unit, MovementAspect aspect) {
        if(unit.getAspect(PlanningAspect.class). != null) { // creature has

        }
        if(path == null)
        path = target != null ? aStar.makeShortestPath(entity, target, planning.isTargetExact()) : null;
        return (path == null) == (target == null); // target and path should be both either set or null.
        return aspect.path != null && !aspect.path.isEmpty(); // path exists
    }

    /**
     *
     */
    private Vector3 getDirectionVector(Position from, Position to) {
        return new Vector3(to.x - from.x, to.y - from.y, to.z - from.z);
    }

    /**
     * Returns vector with [0:1] floats, representing current progress of movement.
     */
    public Vector3 getStepProgressVector() {
        if (!checkPath()) return new Vector3(); // zero vector for staying still.
        Position nextPosition = path.get(0);
        Position unitPosition = entity.position;
        return new Vector3(
                getStepProgressVectorComponent(unitPosition.x, nextPosition.x),
                getStepProgressVectorComponent(unitPosition.y, nextPosition.y),
                getStepProgressVectorComponent(unitPosition.z, nextPosition.z));
    }

    private float getStepProgressVectorComponent(int from, int to) {
        return (to - from) * stepProgress / movementDelay;
    }
}
