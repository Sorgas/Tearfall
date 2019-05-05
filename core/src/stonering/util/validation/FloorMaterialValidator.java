package stonering.util.validation;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.materials.MaterialMap;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

/**
 * Validates that the floor in the tile is made of specified material.
 *
 * @author Alexander on 05.05.2019.
 */
public class FloorMaterialValidator extends PositionValidator {
    private String materialTag;

    public FloorMaterialValidator(String materialTag) {
        this.materialTag = materialTag;
    }

    @Override
    public boolean validate(LocalMap map, Position position) {
        return map.inMap(position) &&
                map.getBlockType(position) == BlockTypesEnum.FLOOR.CODE &&
                MaterialMap.getInstance().getMaterial(map.getMaterial(position)).getTags().contains(materialTag);
    }
}
