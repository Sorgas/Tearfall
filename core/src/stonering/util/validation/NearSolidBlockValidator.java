package stonering.util.validation;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.BuildingContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

/**
 * Checks that near target position exists at least one non-SPACE block, and target position is free from buildings.
 * Used for constructions.
 */
public class NearSolidBlockValidator extends PositionValidator {
    @Override
    public boolean validate(LocalMap map, Position position) {
        return GameMvc.instance().getModel().get(BuildingContainer.class).getBuildingBlocks().get(position) == null && // building-free
                (map.getBlockType(position) == BlockTypesEnum.FLOOR.CODE ||          // floor or space
                        ((map.getBlockType(position.x + 1, position.y, position.z) != 0 ||        // not empty blocks near
                                map.getBlockType(position.x - 1, position.y, position.z) != 0 ||
                                map.getBlockType(position.x, position.y + 1, position.z) != 0 ||
                                map.getBlockType(position.x, position.y - 1, position.z) != 0) &&
                                map.getBlockType(position) == BlockTypesEnum.SPACE.CODE
                        )
                );
    }
}
