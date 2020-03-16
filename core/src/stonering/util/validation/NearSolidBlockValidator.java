package stonering.util.validation;

import stonering.enums.blocks.BlockTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.building.BuildingContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

/**
 * Checks that near target position exists at least one non-SPACE block, and target position is free from buildings.
 * Diagonal tiles do not count. Used for constructions.
 */
public class NearSolidBlockValidator implements PositionValidator {

    @Override
    public Boolean apply(Position position) {
        LocalMap map = GameMvc.model().get(LocalMap.class);
        return GameMvc.model().get(BuildingContainer.class).buildingBlocks.get(position) == null && // building-free
                (map.getBlockType(position) == BlockTypeEnum.FLOOR.CODE ||          // floor or space
                        ((map.getBlockType(position.x + 1, position.y, position.z) != 0 ||        // not empty blocks near
                                map.getBlockType(position.x - 1, position.y, position.z) != 0 ||
                                map.getBlockType(position.x, position.y + 1, position.z) != 0 ||
                                map.getBlockType(position.x, position.y - 1, position.z) != 0) &&
                                map.getBlockType(position) == BlockTypeEnum.SPACE.CODE
                        )
                );
    }
}
