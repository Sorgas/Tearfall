package stonering.util.validation;

import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.materials.MaterialMap;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

/**
 * Checks that new farm zone can be designated in the tile.
 * Designating upon existing farm will reassign tiles to new farm.
 * Old plants will be cut.
 *
 * @author Alexander_Kuzyakov on 05.07.2019.
 */
public class FarmValidator implements PositionValidator {
    private static final String SOIL_TAG = "soil";

    @Override
    public boolean validate(Position position) {
        LocalMap localMap = GameMvc.instance().model().get(LocalMap.class);
        if ((localMap.getBlockType(position) == BlockTypeEnum.FLOOR.CODE ||
                localMap.getBlockType(position) == BlockTypeEnum.FARM.CODE) && // tile is floor or farm
                MaterialMap.instance().getMaterial(localMap.getMaterial(position)).tags.contains(SOIL_TAG)) { // tile is soil
            return true;
        }
        return false;
    }
}
