package stonering.game.model.lists.tasks;

import stonering.entity.plants.PlantBlock;
import stonering.enums.ZoneTypesEnum;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.lists.PlantContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

import static stonering.enums.blocks.BlockTypesEnum.*;

/**
 * Validates applying given designation type to position.
 *
 * @author Alexander on 24.04.2019.
 */
public class DesignationsValidator {

    public boolean validateDesignations(Position position, DesignationTypeEnum type) {
        LocalMap localMap = GameMvc.instance().getModel().get(LocalMap.class);
        BlockTypesEnum blockOnMap = getType(localMap.getBlockType(position));
        switch (type) {
            case DIG: { //makes floor
                return RAMP.equals(blockOnMap) ||
                        WALL.equals(blockOnMap) ||
                        STAIRS.equals(blockOnMap);
            }
            case CHANNEL: { //makes space and ramp lower
                return !SPACE.equals(blockOnMap);
            }
            case STAIR_FLOOR:
                break;
            case STAIRS_COMBINED:
                break;
            case RAMP: {
                return WALL.equals(blockOnMap);
            }
            case STAIRS: {
                return WALL.equals(blockOnMap) ||
                        RAMP.equals(blockOnMap);

//                return WALL.equals(blockOnMap) ||
//                        FLOOR.equals(blockOnMap) ||
//                        FARM.equals(blockOnMap) ||
//                        RAMP.equals(blockOnMap) ||
//                        STAIRS.equals(blockOnMap);
            }
            case CHOP:
            case HARVEST:
            case CUT:
                return validatePlantDesignations(position, blockOnMap, type);
            case BUILD:
                break;
            case NONE: {
                return true;
            }
        }
        return false;
    }

    private boolean validatePlantDesignations(Position position, BlockTypesEnum blockOnMap, DesignationTypeEnum type) {
        switch (type) {
            case CHOP: {
                //TODO designate tree as whole
                PlantContainer container = GameMvc.instance().getModel().get(PlantContainer.class);
                return ((SPACE.equals(blockOnMap) || FLOOR.equals(blockOnMap)))
                        && container.isPlantBlockExists(position)
                        && container.getPlantBlock(position).getPlant().getType().isTree();
            }
            case HARVEST:
                //TODO add harvesting from trees
                PlantBlock block = GameMvc.instance().getModel().get(PlantContainer.class).getPlantBlock(position);
                return !block.getPlant().getType().isTree() && !block.getPlant().getType().isSubstrate();
        }
    }
}
