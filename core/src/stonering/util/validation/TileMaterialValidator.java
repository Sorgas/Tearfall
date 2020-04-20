package stonering.util.validation;

import stonering.enums.materials.MaterialMap;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

/**
 * Validates that the tile is made of specified material.
 *
 * @author Alexander on 05.05.2019.
 */
public class TileMaterialValidator implements PositionValidator {
    private String materialTag;

    public TileMaterialValidator(String materialTag) {
        this.materialTag = materialTag;
    }

    @Override
    public Boolean apply(Position position) {
        LocalMap map = GameMvc.model().get(LocalMap.class);
        return map.inMap(position) &&
                MaterialMap.instance().getMaterial(map.blockType.getMaterial(position)).tags.contains(materialTag);
    }
}
