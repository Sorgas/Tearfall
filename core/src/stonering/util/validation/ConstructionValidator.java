package stonering.util.validation;

import stonering.enums.blocks.BlockTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.building.BuildingContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.IntVector2;
import stonering.util.geometry.Position;

/**
 * Checks that:
 * 1. target is adjacent to non-SPACE block, or another designated construction,
 * 2. target is not occupied with other buildings.
 * Diagonal tiles do not count.
 */
public class ConstructionValidator implements PositionValidator {

    @Override
    public Boolean apply(Position position) {

        LocalMap map = GameMvc.model().get(LocalMap.class);
        return GameMvc.model().get(BuildingContainer.class).buildingBlocks.get(position) == null && // building-free
                (map.blockType.get(position) == BlockTypeEnum.FLOOR.CODE ||          // floor or space
                        ((map.blockType.get(position.x + 1, position.y, position.z) != 0 ||        // not empty blocks near
                                map.blockType.get(position.x - 1, position.y, position.z) != 0 ||
                                map.blockType.get(position.x, position.y + 1, position.z) != 0 ||
                                map.blockType.get(position.x, position.y - 1, position.z) != 0) &&
                                map.blockType.get(position) == BlockTypeEnum.SPACE.CODE
                        )
                );
    }
}
