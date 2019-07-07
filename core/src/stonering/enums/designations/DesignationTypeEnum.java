package stonering.enums.designations;

import java.util.HashMap;

/**
 * Enum of designation types. All build designation are combined, because
 * they are one order logically and detailed with building name provided on task creation.
 *
 * @author Alexander Kuzyakov
 */
public enum DesignationTypeEnum {
    NONE(0, "none"),
    DIG(1, "digging wall"), // removes walls and ramps. leaves floor
    STAIRS(2, "cutting stairs"), // cuts stairs from wall.
    RAMP(3, "cutting ramp"), // digs ramp and upper cell.
    CHANNEL(4, "digging channel"), // digs cell and ramp on lower level
    CHOP(5, "chopping trees"), // chop trees in th area
    CUT(6, "cutting plants"), // cut plants
    HARVEST(7, "harvesting plants"), // harvest plants
    BUILD(8, "building"); // build construction or building

    private static HashMap<Byte, DesignationTypeEnum> map;
    public final byte CODE;
    private String text;

    static {
        map = new HashMap<>();
        for (DesignationTypeEnum type : DesignationTypeEnum.values()) {
            map.put(type.CODE, type);
        }
    }

    DesignationTypeEnum(int code, String text) {
        this.CODE = (byte) code;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public DesignationTypeEnum getType(int code) {
        return map.get(code);
    }
}
