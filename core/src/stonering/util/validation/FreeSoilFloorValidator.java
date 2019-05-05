package stonering.util.validation;

import stonering.enums.materials.MaterialMap;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

/**
 * Checks that tile is free floor and consists of soil of desired type.
 *
 * @author Alexander on 18.03.2019.
 */
public class FreeSoilFloorValidator extends FreeFloorValidator {
    private static final String SOIL_TAG = "soil";

    @Override
    public boolean validate(LocalMap localMap, Position position) {
        return super.validate(localMap, position) &&
                MaterialMap.getInstance().getMaterial(localMap.getMaterial(position)).getTags().contains(SOIL_TAG);
    }
}
