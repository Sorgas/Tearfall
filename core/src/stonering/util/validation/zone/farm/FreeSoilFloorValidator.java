package stonering.util.validation.zone.farm;

import stonering.enums.materials.MaterialMap;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.util.validation.FreeFloorValidator;

/**
 * Checks that tile is free floor and consists of soil of desired type.
 *
 * @author Alexander on 18.03.2019.
 */
public class FreeSoilFloorValidator extends FreeFloorValidator {

    @Override
    public Boolean apply(Position position) {
        LocalMap localMap = GameMvc.model().get(LocalMap.class);
        return super.apply(position) && "soil".equals(MaterialMap.getMaterial(localMap.blockType.getMaterial(position)).name);
    }
}
