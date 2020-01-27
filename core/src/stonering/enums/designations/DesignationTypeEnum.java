package stonering.enums.designations;

import stonering.util.validation.DiggingValidator;
import stonering.util.validation.PositionValidator;
import stonering.util.validation.TreeChoppingValidator;

import java.util.HashMap;

import static stonering.enums.blocks.BlockTypeEnum.*;

/**
 * Enum of designation types for simple orders like digging, cutting plants, etc.
 * Has validators to check designation creation and performing. Job requirements are hard-coded in comprehensive {@link stonering.entity.job.action.Action}.
 *
 * TODO add icons for designations
 * @author Alexander Kuzyakov
 */
public enum DesignationTypeEnum {
    D_NONE(0, "none", position -> true), // for removing simple designations
    D_DIG(1, "digging", new DiggingValidator(FLOOR)), // removes walls and ramps. leaves floor
    D_STAIRS(2, "cutting stairs", new DiggingValidator(STAIRS)), // cuts stairs from wall.
    D_DOWNSTAIRS(4, "cutting downstairs", new DiggingValidator(DOWNSTAIRS)), // cuts combined stairs from wall. assigned automatically.
    D_RAMP(5, "cutting ramp", new DiggingValidator(RAMP)), // digs ramp and upper cell.
    D_CHANNEL(6, "digging channel", new DiggingValidator(SPACE)), // digs cell and ramp on lower level
    D_CHOP(2, "chopping trees", new TreeChoppingValidator()), // chop trees in th area
    D_CUT(3, "cutting plants", position -> true), // cut plants
    D_HARVEST(4, "harvesting plants", position -> true), // harvest plants
    D_BUILD(5, "building", position -> true), // build construction or building
    ;

    private static HashMap<Integer, DesignationTypeEnum> map;
    public final int CODE;
    public final String iconName = null;
    public final String text;
    public final PositionValidator validator;

    static {
        map = new HashMap<>();
        for (DesignationTypeEnum type : DesignationTypeEnum.values()) {
            map.put(type.CODE, type);
        }
    }

    DesignationTypeEnum(int code, String text, PositionValidator validator) {
        this.CODE = (byte) code;
        this.text = text;
        this.validator = validator;
    }

    public DesignationTypeEnum getType(int code) {
        return map.get(code);
    }
}
