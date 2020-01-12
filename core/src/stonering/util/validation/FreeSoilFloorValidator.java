package stonering.util.validation;

import stonering.enums.materials.MaterialMap;
import stonering.game.GameMvc;
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
    public boolean validate(Position position) {
        LocalMap localMap = GameMvc.instance().model().get(LocalMap.class);
        return super.validate(position) && MaterialMap.instance().getMaterial(localMap.getMaterial(position)).tags.contains(SOIL_TAG);
    }
}
