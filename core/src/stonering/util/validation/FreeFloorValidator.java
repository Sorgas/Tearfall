package stonering.util.validation;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.model.system.building.BuildingContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

/**
 * Checks if desired tile is free floor.
 *
 * @author Alexander on 23.11.2018.
 */
public class FreeFloorValidator implements PositionValidator {
    public static final String NAME = "floor";

    @Override
    public boolean validate(Position position) {
        GameModel model = GameMvc.instance().model();
        return model.get(LocalMap.class).getBlockType(position) == BlockTypesEnum.FLOOR.CODE &&
                model.get(BuildingContainer.class).getBuildingBlocks().get(position) == null;
    }
}
