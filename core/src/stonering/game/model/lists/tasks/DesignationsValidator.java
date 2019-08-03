package stonering.game.model.lists.tasks;

import stonering.entity.plants.PlantBlock;
import stonering.enums.ZoneTypesEnum;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.lists.PlantContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

/**
 * Validates applying given designation type to position.
 *
 * @author Alexander on 24.04.2019.
 */
public class DesignationsValidator {

    public boolean validateDesignations(Position position, DesignationTypeEnum type) {
        LocalMap localMap = GameMvc.instance().getModel().get(LocalMap.class);
        BlockTypesEnum blockOnMap = BlockTypesEnum.getType(localMap.getBlockType(position));
        switch (type) {
            case DIG: { //makes floor
                return BlockTypesEnum.RAMP.equals(blockOnMap) ||
                        BlockTypesEnum.WALL.equals(blockOnMap) ||
                        BlockTypesEnum.STAIRS.equals(blockOnMap);
            }
            case CHANNEL: { //makes space and ramp lower
                return !BlockTypesEnum.SPACE.equals(blockOnMap);
            }
            case RAMP: {
                return BlockTypesEnum.WALL.equals(blockOnMap);
            }
            case STAIRS: {
                return BlockTypesEnum.WALL.equals(blockOnMap) ||
                        BlockTypesEnum.FLOOR.equals(blockOnMap) ||
                        BlockTypesEnum.FARM.equals(blockOnMap) ||
                        BlockTypesEnum.RAMP.equals(blockOnMap);
            }
            case CHOP: {
                //TODO designate tree as whole
                PlantContainer container = GameMvc.instance().getModel().get(PlantContainer.class);
                return ((BlockTypesEnum.SPACE.equals(blockOnMap) || BlockTypesEnum.FLOOR.equals(blockOnMap)))
                        && container.isPlantBlockExists(position)
                        && container.getPlantBlock(position).getPlant().getType().isTree();
            }
            case NONE: {
                return true;
            }
            case CUT:
                break;
            case HARVEST:
                //TODO add harvesting from trees
                PlantBlock block = GameMvc.instance().getModel().get(PlantContainer.class).getPlantBlock(position);
                return !block.getPlant().getType().isTree() && !block.getPlant().getType().isSubstrate();
            case BUILD:
                break;
        }
        return false;
    }
}
