package stonering.util.validation;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.plant.PlantContainer;
import stonering.util.geometry.Position;

import static stonering.enums.blocks.BlockTypesEnum.FLOOR;
import static stonering.enums.blocks.BlockTypesEnum.SPACE;

/**
 * Checks if there is a tree or plant in a given tile.
 *
 * @author Alexander on 23.01.2020
 */
public class PlantCuttingValidator implements PositionValidator {

    @Override
    public boolean validate(Position position) {
        PlantContainer container = GameMvc.model().get(PlantContainer.class);
        BlockTypesEnum blockOnMap = GameMvc.model().get(LocalMap.class).getBlockTypeEnumValue(position);
        return ((SPACE.equals(blockOnMap) || FLOOR.equals(blockOnMap))) && container.isPlantBlockExists(position);
    }
}
