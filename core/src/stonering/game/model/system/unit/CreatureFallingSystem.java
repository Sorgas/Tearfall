package stonering.game.model.system.unit;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.MovementAspect;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.EntitySystem;
import stonering.util.geometry.Position;

/**
 * System, that makes creatures in SPACE tiles fall down.
 * 
 * @author Alexander on 23.10.2019.
 */
public class CreatureFallingSystem extends EntitySystem<Unit> {
    private UnitContainer container;
    private LocalMap map;

    @Override
    public void update(Unit unit) {
        container = GameMvc.model().get(UnitContainer.class);
        map = GameMvc.model().get(LocalMap.class);
        if(canFall(unit)) fall(unit);
    }

    /**
     * Creature can fall, if is in space cell, and cell below is fly passable.
     */
    private boolean canFall(Unit unit) {
        Position pos = unit.position;
        return map.blockType.getEnumValue(pos) == BlockTypeEnum.SPACE && // can fall through SPACE
                pos.z > 0 && // not the bottom of a map
                map.isFlyPassable(pos.x, pos.y, pos.z - 1); // lower tile is open
    }

    /**
     * Moves creature lower, if it is above ground.
     * Deletes target and path, for recalculation on next iteration.
     * //TODO apply fall damage
     */
    private void fall(Unit unit) {
        Position pos = unit.position;
        container.updateUnitPosition(unit, new Position(pos.x, pos.y, pos.z - 1));
        unit.get(MovementAspect.class).path = null;
    }
}
