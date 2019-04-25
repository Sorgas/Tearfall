package stonering.util.validation;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.model.local_map.LocalMap;
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
