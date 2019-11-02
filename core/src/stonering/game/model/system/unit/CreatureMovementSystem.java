package stonering.game.model.system.unit;

import com.badlogic.gdx.math.Vector3;
import stonering.entity.FloatPositionEntity;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.MovementAspect;
import stonering.entity.unit.aspects.PlanningAspect;
import stonering.enums.OrderStatusEnum;
import stonering.enums.TaskStatusEnum;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;
import stonering.util.pathfinding.a_star.AStar;

/**
 * Moves all units across the map. General algorithm:
 * Update current target with target from {@link PlanningAspect}, update path if needed.
 * Change vectorPosition of a unit, by its speed parameter in direction of a next tile in the path (integer position will change, see {@link FloatPositionEntity}).
 * Algorithm:
 * Checks if target position in planning aspect has changed, creates new path if needed.
 * Moves unit to the next tile in the path, removes tile from the path if it's reached.
 * <p>
 * Stores action target position got from planning aspect, to avoid making path on every update.
 * When no path is present, moves units to the 'center' of a tile, by updating their vector position to integer position.
 * Integer position of an entity is a result of rounding of it's vector position.
 * Planning aspect will update target when its reached.
 *
 * @author Alexander on 21.10.2019.
 */
public class CreatureMovementSystem {
    private LocalMap localMap;
    private UnitContainer unitContainer;
    private AStar aStar;

    public void update(Unit unit) {
        GameModel model = GameMvc.instance().getModel();
        localMap = model.get(LocalMap.class);
        unitContainer = model.get(UnitContainer.class);
        aStar = model.get(AStar.class);
        MovementAspect aspect = unit.getAspect(MovementAspect.class);
        if (checkPath(unit, aspect)) {
            makeStep(unit, aspect);
        } else {
            moveToTileCenter(unit, aspect);
        }
    }

    /**
     * Checks that path to target exists. Creates new path if needed. Can fail task in {@link PlanningAspect}.
     */
    private boolean checkPath(Unit unit, MovementAspect movement) {
        PlanningAspect planning = unit.getAspect(PlanningAspect.class);
        if (!planning.movementNeeded) return freeAspect(movement);
        Position target = planning.getTarget();
        if (!target.equals(movement.target) || movement.path == null) { // target has changed, or path is null for old target
            movement.target = target;
            movement.path = aStar.makeShortestPath(unit.position, movement.target = planning.getTarget());
            if (movement.path == null) {
                planning.task.status = TaskStatusEnum.FAILED; // no path to target, fail task
                return freeAspect(movement);
            }
        }
        return true; // target is old and path exists
    }

    /**
     * Moves creature to the next tile from path.
     */
    private void makeStep(Unit unit, MovementAspect aspect) {
        Position nextPosition = aspect.path.get(0);
        if (localMap.isWalkPassable(nextPosition)) { // path has not been blocked after calculation
            Vector3 direction = getDirectionVector(unit.vectorPosition, nextPosition);
            if (direction.len() > aspect.speed)
                direction.setLength(aspect.speed); // if distance to next position is less than unit's speed, it will be covered in 1 step
            unit.vectorPosition.add(direction);
            unitContainer.updateUnitPosiiton(unit, unit.vectorPosition); // change unit position in container
            if (nextPosition.equals(unit.vectorPosition)) { // next tile reached
                aspect.path.remove(0); // remove reached tile from path
                unitContainer.healthSystem.applyMoveChange(unit);
            }
        } else { // path blocked
            Logger.PATH.log("path was blocked in " + nextPosition);
            aspect.path = null; // drop path, will be recounted on next update
        }
    }

    /**
     * Moves units to center of their current tiles if needed.
     */
    private void moveToTileCenter(Unit unit, MovementAspect aspect) {
        if (localMap.isWalkPassable(unit.position)) { // path has not been blocked after calculation
            if (!unit.position.equals(unit.vectorPosition)) {
                Vector3 direction = getDirectionVector(unit.vectorPosition, unit.position);
                if (direction.len() > aspect.speed) direction.setLength(aspect.speed);
                Vector3 newVectorPosition = unit.vectorPosition.cpy().add(direction);
                unitContainer.updateUnitPosiiton(unit, newVectorPosition); // change unit position in container
            }
        } else { // path blocked
            Logger.PATH.log("Unit's position " + unit.position + " is impassable");
            aspect.path = null; // drop path, will be recounted on next update
        }
    }

    private boolean freeAspect(MovementAspect aspect) {
        aspect.path = null;
        aspect.target = null;
        return false;
    }

    private Vector3 getDirectionVector(Vector3 from, Position to) {
        return new Vector3(to.x - from.x, to.y - from.y, to.z - from.z);
    }
}
