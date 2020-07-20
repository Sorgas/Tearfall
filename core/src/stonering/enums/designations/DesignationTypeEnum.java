package stonering.enums.designations;

import stonering.enums.unit.JobMap;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.util.logging.Logger;
import stonering.util.validation.*;

import java.util.HashMap;

import static stonering.enums.blocks.BlockTypeEnum.*;

/**
 * Enum of designation types for simple orders like digging, cutting plants, etc.
 * Has validators to check designation creation and performing. Job requirements are hard-coded in comprehensive {@link stonering.entity.job.action.Action}.
 *
 * TODO add tiles for designations
 * @author Alexander Kuzyakov
 */
public enum DesignationTypeEnum {
    D_NONE(0, "none", position -> true, 1, null),                                     // for removing simple designations
    D_DIG(1, "digging", new DiggingValidator(FLOOR), 2, "miner"),                        // removes walls and ramps. leaves floor
    D_STAIRS(2, "cutting stairs", new DiggingValidator(STAIRS), 3, "miner"),             // cuts stairs from wall.
    D_DOWNSTAIRS(4, "cutting downstairs", new DiggingValidator(DOWNSTAIRS), 4, "miner"), // cuts combined stairs from wall. assigned automatically.
    D_RAMP(5, "cutting ramp", new DiggingValidator(RAMP), 5, "miner"),                   // digs ramp and upper cell.
    D_CHANNEL(6, "digging channel", new DiggingChannelValidator(), 6, "miner"),          // digs cell and ramp on lower level
    D_CHOP(2, "chopping trees", new TreeChoppingValidator(), 7, "lumberjack"),                // chop trees in th area
    D_CUT(3, "cutting plants", position -> true, 8, "herbalist"),                            // cut plants
    D_HARVEST(4, "harvesting plants", position -> true, 9, "herbalist"),                     // harvest plants
    D_HOE(5, "hoeing", new FreeSoilFloorValidator(), 11, "farmer"),
    D_BUILD(5, "building", position -> true, 10, "builder"),                               // build construction or building
    D_PLANT(5, "planting", PlaceValidatorsEnum.FARM.VALIDATOR, 12, "farmer")
    ;

    private static HashMap<Integer, DesignationTypeEnum> map;
    public final int CODE;
    public final String TEXT;
    public final PositionValidator VALIDATOR;
    public final int TOOL_SPRITE; // atlas x for sprite
    public final String JOB;

    static {
        map = new HashMap<>();
        for (DesignationTypeEnum type : DesignationTypeEnum.values()) {
            map.put(type.CODE, type);
        }
    }

    DesignationTypeEnum(int code, String text, PositionValidator validator, int toolSprite, String job) {
        CODE = (byte) code;
        TEXT = text;
        VALIDATOR = validator;
        TOOL_SPRITE = toolSprite;
        if(JobMap.get(job) == null) Logger.LOADING.logWarn("Designation job " + job + " not found.");
        JOB = job;
    }

    public DesignationTypeEnum getType(int code) {
        return map.get(code);
    }
}
