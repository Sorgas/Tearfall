package stonering.util.validation;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.materials.MaterialMap;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

/**
 * Checks that new farm zone can be designated in the tile.
 * Designating upon existing farm will reassign tiles to new farm.
 * Old plants will be cut.
 *
 * @author Alexander_Kuzyakov on 05.07.2019.
 */
public class FarmValidator extends PositionValidator {
    private static final String SOIL_TAG = "soil";

    @Override
    public boolean validate(LocalMap localMap, Position position) {
        if ((localMap.getBlockType(position) == BlockTypesEnum.FLOOR.CODE ||
                localMap.getBlockType(position) == BlockTypesEnum.FARM.CODE) && // tile is floor or farm
                MaterialMap.instance().getMaterial(localMap.getMaterial(position)).getTags().contains(SOIL_TAG)) { // tile is soil
            return true;
        }
        return false;
    }
}
