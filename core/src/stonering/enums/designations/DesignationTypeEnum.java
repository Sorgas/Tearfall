package stonering.enums.designations;

import stonering.enums.unit.JobMap;
import stonering.util.logging.Logger;
import stonering.util.validation.*;

import java.util.HashMap;

import static stonering.enums.blocks.BlockTypeEnum.*;

/**
 * Enum of designation types for simple orders like digging, cutting plants, etc.
 * Has validators to check designation creation and performing. Job requirements are hard-coded in comprehensive {@link stonering.entity.job.action.Action}.
 * <p>
 * TODO add tiles for designations
 *
 * @author Alexander Kuzyakov
 */
public enum DesignationTypeEnum {
    D_NONE(-1, "none", null),                                                          // for removing simple designations

    D_DIG(2, "digging", new DiggingValidator(FLOOR), "miner"),                        // removes walls and ramps. leaves floor
    D_STAIRS(3, "cutting stairs", new DiggingValidator(STAIRS), "miner"),             // cuts stairs from wall.
    D_DOWNSTAIRS(4, "cutting downstairs", new DiggingValidator(DOWNSTAIRS), "miner"), // cuts combined stairs from wall. assigned automatically.
    D_RAMP(5, "cutting ramp", new DiggingValidator(RAMP), "miner"),                   // digs ramp and upper cell.
    D_CHANNEL(6, "digging channel", new DiggingChannelValidator(), "miner"),          // digs cell and ramp on lower level

    D_CHOP(7, "chopping trees", PlaceValidatorEnum.TREE_EXISTS.VALIDATOR, "lumberjack"),     // chop trees in th area
    D_CUT(8, "cutting plants", "herbalist"),                                          // cut plants
    D_HARVEST(9, "harvesting plants", "herbalist"),                                   // harvest plants
    D_BUILD(10, "building", "builder"),                                               // build construction or building
    D_HOE(11, "hoeing", PlaceValidatorEnum.SOIL_FLOOR.VALIDATOR, "farmer"),
    D_CUT_FARM(12, "cutting plants", "farmer"),                                          // cut unwanted plants from farm
    D_PLANT(13, "planting", PlaceValidatorEnum.FARM.VALIDATOR, "farmer");

    private static HashMap<Integer, DesignationTypeEnum> map;
    public final int SPRITE_X;
    public final String TEXT;
    public final PositionValidator VALIDATOR;
    public final String JOB;

    static {
        map = new HashMap<>();
        for (DesignationTypeEnum type : DesignationTypeEnum.values()) {
            map.put(type.SPRITE_X, type);
        }
    }

    DesignationTypeEnum(int spriteX, String text, PositionValidator validator, String job) {
        SPRITE_X = (byte) spriteX;
        TEXT = text;
        VALIDATOR = validator;
        if (JobMap.get(job) == null)
            Logger.LOADING.logWarn("Job " + job + " for designation type " + text + " not found.");
        JOB = job;
    }

    DesignationTypeEnum(int spriteX, String text, String job) {
        this(spriteX, text, position -> true, job);
    }

    DesignationTypeEnum(int spriteX, String text, PlaceValidatorEnum placeValidatorEnum, String job) {
        this(spriteX, text, placeValidatorEnum.VALIDATOR, job);
    }

    public DesignationTypeEnum getType(int code) {
        return map.get(code);
    }
}
