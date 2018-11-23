package stonering.entity.local.building.validators;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.core.model.LocalMap;
import stonering.global.utils.Position;

/**
 * Checks if desired tile is free floor.
 *
 * @author Alexander on 23.11.2018.
 */
public class FreeFloorValidator extends PositionValidator {

    @Override
    public boolean validate(LocalMap localMap, Position position) {
        return localMap.getBlockType(position) == BlockTypesEnum.FLOOR.getCode() &&
                localMap.getBuildingBlock(position) == null;
    }
}
