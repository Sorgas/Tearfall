package stonering.game.model.system.unit;

import com.badlogic.gdx.math.Vector3;
import stonering.entity.VectorPositionEntity;
import stonering.entity.job.action.target.ActionTarget;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.MovementAspect;
import stonering.entity.unit.aspects.PlanningAspect;
import stonering.enums.action.TaskStatusEnum;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.EntitySystem;
import stonering.game.model.system.task.CreatureTaskPerformingSystem;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;
import stonering.util.pathfinding.a_star.AStar;

/**
 * Moves all units across the map.
 * Works only with target and path defined in {@link MovementAspect} (target for movement is set in {@link CreatureTaskPerformingSystem}).
 * <p>
 * General algorithm:
 * If there is no target do nothing.
 * If there is a target but no path, create path, task is failed if no path is found.
 * If there is a target and path, move, drop path if it is blocked. Path is recreated on next update.
 * <p>
 * Moves unit to the next tile in the path, removes tile from the path if it's reached.
 * Change vectorPosition of a unit, by its speed parameter in direction of a next tile in the path (integer position will change, see {@link VectorPositionEntity}).
 * When no path is present, moves units to the 'center' of a tile, by updating their vector position to integer position.
 * Integer position of an entity is a result of rounding of it's vector position.
 * Planning aspect will update target when its reached.
 *
 * @author Alexander on 21.10.2019.
 */
public class CreatureMovementSystem extends EntitySystem<Unit> {
    private LocalMap localMap;
    private UnitContainer unitContainer;
    private AStar aStar;

    @Override
    public void update(Unit unit) {
        GameModel model = GameMvc.model();
        localMap = model.get(LocalMap.class);
        unitContainer = model.get(UnitContainer.class);
        aStar = model.get(AStar.class);
        MovementAspect movement = unit.getAspect(MovementAspect.class);
        if (movement.target != null) {
            if (checkPath(unit, movement)) makeStep(unit, movement);
        } else {
            moveToTileCenter(unit, movement);
        }
    }

    /**
     * Checks that path to target exists. Creates new path if needed. Can fail task in {@link PlanningAspect}.
     */
    private boolean checkPath(Unit unit, MovementAspect movement) {
        PlanningAspect planning = unit.getAspect(PlanningAspect.class);
        if (movement.path == null) { // path was blocked or not created
            movement.path = aStar.makeShortestPath(unit.position, movement.target, planning.task.nextAction.actionTarget.targetType);
            if (movement.path == null) {
                planning.task.status = TaskStatusEnum.FAILED; // no path to target, fail task
                return freeAspect(movement);
            }
        }
        return true; // path exists
    }

    /**
     * Finds new movement target and path.
     */
    private void updateMovementAspect(Unit unit, MovementAspect movement, PlanningAspect planning) {
        ActionTarget actionTarget = planning.task.nextAction.actionTarget;
        switch (actionTarget.targetType) {
            case EXACT:
                break;
            case NEAR:
                break;
            case ANY:
                break;
        }
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
                if(aspect.path.isEmpty()) freeAspect(aspect); // path is finished
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
