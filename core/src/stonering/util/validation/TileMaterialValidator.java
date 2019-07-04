package stonering.util.validation;

import stonering.enums.materials.MaterialMap;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

/**
 * Validates that the tile is made of specified material.
 *
 * @author Alexander on 05.05.2019.
 */
public class TileMaterialValidator extends PositionValidator {
    private String materialTag;

    public TileMaterialValidator(String materialTag) {
        this.materialTag = materialTag;
    }

    @Override
    public boolean validate(LocalMap map, Position position) {
        return map.inMap(position) &&
                MaterialMap.instance().getMaterial(map.getMaterial(position)).getTags().contains(materialTag);
    }
}
