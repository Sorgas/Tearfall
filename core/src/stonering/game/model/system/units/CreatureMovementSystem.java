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
 * Moves all units across the map. General algorithm:
 * Update current target with target from {@link PlanningAspect}, update path if needed.
 * Change vectorPosition of a unit, by its speed parameter in direction of a next tile in the path (integer position will change, see {@link FloatPositionEntity}).
 * Algorithm:
 *     Checks if target position in planning aspect has changed, creates new path if needed.
 *     Moves unit to the next tile in the path, removes tile from the path if it's reached.
 *
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

    public void updateUnitPosition(Unit unit) {
        GameModel model = GameMvc.instance().getModel();
        localMap = model.get(LocalMap.class);
        unitContainer = model.get(UnitContainer.class);
        aStar = model.get(AStar.class);
        MovementAspect aspect = unit.getAspect(MovementAspect.class);
        if (checkPath(unit, aspect)) makeStep(unit, aspect);
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
            aspect.path = null; // drop path, will be recounted on next update
        }
    }

    /**
     * Checks that path to target exists. Creates new path if needed. Can fail task in {@link PlanningAspect}.
     */
    private boolean checkPath(Unit unit, MovementAspect movement) {
        PlanningAspect planning = unit.getAspect(PlanningAspect.class);
        if (!planning.isMovementNeeded()) return freeAspect(movement);
        Position target = planning.getTarget();
        if(!target.equals(movement.target) || movement.path == null) { // target has changed, or path is null for old target
            movement.target = target;
            movement.path = aStar.makeShortestPath(unit.position, movement.target = planning.getTarget());
            if(movement.path == null) {
                planning.interrupt(); // no path to target, fail task
                return freeAspect(movement);
            }
        }
        return true; // target is old and path exists
    }

    private boolean freeAspect(MovementAspect aspect) {
        aspect.path = null;
        aspect.target = null;
        return false;
    }

    private Vector3 getDirectionVector(Position from, Position to) {
        return new Vector3(to.x - from.x, to.y - from.y, to.z - from.z);
    }
}
