package stonering.entity.local.building.validators;

import stonering.enums.materials.MaterialMap;
import stonering.game.core.model.local_map.LocalMap;
import stonering.util.geometry.Position;

public class FreeSoilFloorValidator extends FreeFloorValidator {
    private static final String SOIL_TAG = "soil";

    @Override
    public boolean validate(LocalMap localMap, Position position) {
        return super.validate(localMap, position) &&
                MaterialMap.getInstance().getMaterial(localMap.getMaterial(position)).getTags().contains(SOIL_TAG);
    }
}
