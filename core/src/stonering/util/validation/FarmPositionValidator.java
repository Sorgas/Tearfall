package stonering.util.validation;

import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.materials.MaterialMap;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

/**
 * Checks that tile is valid for farm: tile is floor or farm, soil, no buildings.
 * Designating upon existing farm will reassign tiles to new farm.
 * Old plants will be cut.
 *
 * @author Alexander_Kuzyakov on 05.07.2019.
 */
public class FarmPositionValidator implements PositionValidator {
    private static final String SOIL_TAG = "soil";

    @Override
    public Boolean apply(Position position) {
        LocalMap localMap = GameMvc.model().get(LocalMap.class);
        if ((localMap.blockType.get(position) == BlockTypeEnum.FLOOR.CODE ||
                localMap.blockType.get(position) == BlockTypeEnum.FARM.CODE) && // tile is floor or farm
                MaterialMap.getMaterial(localMap.blockType.getMaterial(position)).tags.contains(SOIL_TAG)) { // tile is soil
            return true;
        }
        return false;
    }
}
