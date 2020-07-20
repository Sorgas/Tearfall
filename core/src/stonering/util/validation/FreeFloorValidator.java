package stonering.util.validation;

import stonering.enums.blocks.BlockTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.model.system.building.BuildingContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.plant.PlantContainer;
import stonering.util.geometry.Position;

/**
 * Checks if desired tile is free floor.
 *
 * @author Alexander on 23.11.2018.
 */
public class FreeFloorValidator implements PositionValidator {

    @Override
    public Boolean apply(Position position) {
        GameModel model = GameMvc.model();
        return model.get(LocalMap.class).blockType.get(position) == BlockTypeEnum.FLOOR.CODE
                && model.get(BuildingContainer.class).buildingBlocks.get(position) == null;
    }
}
