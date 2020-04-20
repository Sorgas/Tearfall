package stonering.util.validation;

import stonering.enums.blocks.BlockTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.plant.PlantContainer;
import stonering.util.geometry.Position;

import static stonering.enums.blocks.BlockTypeEnum.FLOOR;
import static stonering.enums.blocks.BlockTypeEnum.SPACE;

/**
 * Checks if there is a tree or plant in a given tile.
 *
 * @author Alexander on 23.01.2020
 */
public class PlantCuttingValidator implements PositionValidator {

    @Override
    public Boolean apply(Position position) {
        PlantContainer container = GameMvc.model().get(PlantContainer.class);
        BlockTypeEnum blockOnMap = GameMvc.model().get(LocalMap.class).blockType.getEnumValue(position);
        return ((SPACE.equals(blockOnMap) || FLOOR.equals(blockOnMap))) && container.isPlantBlockExists(position);
    }
}
