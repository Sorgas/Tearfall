package stonering.util.validation;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.plant.PlantContainer;
import stonering.util.geometry.Position;

import static stonering.enums.blocks.BlockTypesEnum.*;

/**
 * Checks if there is a tree in given tile.
 *
 * @author Alexander on 20.01.2020.
 */
public class TreeChoppingValidator implements PositionValidator {

    @Override
    public boolean validate(Position position) {
        PlantContainer container = GameMvc.model().get(PlantContainer.class);
        BlockTypesEnum blockOnMap = GameMvc.model().get(LocalMap.class).getBlockTypeEnumValue(position);
        return ((SPACE.equals(blockOnMap) || FLOOR.equals(blockOnMap)))
                && container.isPlantBlockExists(position)
                && container.getPlantBlock(position).getPlant().type.isTree;
    }
}
