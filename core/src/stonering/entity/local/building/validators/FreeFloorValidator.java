package stonering.entity.local.building.validators;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.core.model.local_map.LocalMap;
import stonering.util.geometry.Position;

/**
 * Checks if desired tile is free floor.
 *
 * @author Alexander on 23.11.2018.
 */
public class FreeFloorValidator extends PositionValidator {
    public static final String NAME = "floor";

    @Override
    public boolean validate(LocalMap localMap, Position position) {
        return localMap.getBlockType(position) == BlockTypesEnum.FLOOR.CODE &&
                localMap.getBuildingBlock(position) == null;
    }
}
