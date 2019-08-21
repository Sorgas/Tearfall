package stonering.game.model.lists.tasks;

import stonering.entity.plants.PlantBlock;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.lists.PlantContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

import static stonering.enums.blocks.BlockTypesEnum.*;

/**
 * Validates applying given designation type to position.
 *
 * @author Alexander on 24.04.2019.
 */
public class DesignationsValidator {

    public boolean validateDesignation(Position position, DesignationTypeEnum type) {
        LocalMap localMap = GameMvc.instance().getModel().get(LocalMap.class);
        BlockTypesEnum blockOnMap = getType(localMap.getBlockType(position));
        switch (type) {
            case DIG:  //makes floor
            case CHANNEL:  //makes space and ramp lower
            case UPSTAIRS:
            case DOWNSTAIRS:
            case RAMP:
            case STAIRS:
                return blockOnMap.OPENNESS > getTargetBlockType(type).OPENNESS;
            case CHOP:
            case HARVEST:
            case CUT:
                return validatePlantDesignations(position, blockOnMap, type);
            case BUILD:
            case NONE:
                return true;
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
        return false;
    }

    /**
     * Returns block type, created by digging.
     */
    private BlockTypesEnum getTargetBlockType(DesignationTypeEnum designationType) {
        switch (designationType) {
            case DIG:
                return FLOOR;
            case STAIRS:
                return STAIRS;
            case UPSTAIRS:
                return UPSTAIRS;
            case DOWNSTAIRS:
                return DOWNSTAIRS;
            case RAMP:
                return RAMP;
            case CHANNEL:
                return SPACE;
        }
        Logger.TASKS.logError("Digging type not handled in mapping method.");
        return null;
    }
}
