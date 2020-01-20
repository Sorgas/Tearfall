package stonering.game.model.system.task;

import stonering.entity.plant.PlantBlock;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.plant.PlantContainer;
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

    /**
     * Calls correct validation method for designation type.
     */
    public boolean validateDesignation(Position position, DesignationTypeEnum type) {
        LocalMap localMap = GameMvc.instance().model().get(LocalMap.class);
        BlockTypesEnum blockOnMap = getType(localMap.getBlockType(position));
        switch (type) {
            case D_DIG: //makes floor
            case D_CHANNEL: //makes space and ramp lower
            case D_DOWNSTAIRS:
            case D_RAMP:
            case D_STAIRS:
                return validateDigging(position, blockOnMap, type);
            case D_CHOP:
            case D_HARVEST:
            case D_CUT:
                return validatePlantDesignations(position, blockOnMap, type);
            case D_BUILD:
                //TODO
            case D_NONE:
                return true;
        }
        return false;
    }

    public boolean validateDigging(Position position, BlockTypesEnum mapBlock, DesignationTypeEnum type) {
        if (getTargetBlockType(type).OPENNESS > mapBlock.OPENNESS) return true; // tile openness can only increase on digging
        Logger.TASKS.logWarn("Digging of " + type + " is invalid in " + position);
        return false;
    }

    /**
     * Returns block type, created by digging.
     */
    public BlockTypesEnum getTargetBlockType(DesignationTypeEnum designationType) {
        switch (designationType) {
            case D_DIG:
                return FLOOR;
            case D_STAIRS:
                return STAIRS;
            case D_DOWNSTAIRS:
                return DOWNSTAIRS;
            case D_RAMP:
                return RAMP;
            case D_CHANNEL:
                return SPACE;
        }
        Logger.TASKS.logError("Digging type not handled in mapping method.");
        return null;
    }
}
