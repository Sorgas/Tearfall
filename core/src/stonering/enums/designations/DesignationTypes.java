package stonering.enums.designations;

import java.util.HashMap;

/**
 * Enum of designation types. All build designations are combined, because
 * they are one order logically and detailed with building title provided on task creation.
 *
 * @author Alexander Kuzyakov
 */
public enum DesignationTypes {
    NONE(0, "none"),
    DIG(1, "digging wall"), // removes walls and ramps. leaves floor
    STAIRS(2, "cutting stairs"), //cuts stairs from wall.
    RAMP(3, "cutting ramp"), // digs ramp and upper cell.
    CHANNEL(4, "digging channel"), // digs cell and ramp on lower level
    CHOP(5, "chopping trees"), //chop trees in th area
    CUT(6, "cutting plants"), //cut plants
    BUILD(7, "building"); //build construction or building

    private static HashMap<Byte, DesignationTypes> map;
    private byte code;
    private String text;

    static {
        map = new HashMap<>();
        for (DesignationTypes type : DesignationTypes.values()) {
            map.put(type.code, type);
        }
    }

    DesignationTypes(int code, String text) {
        this.code = (byte) code;
        this.text = text;
    }

    public byte getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public DesignationTypes getType(int code) {
        return map.get(code);
    }
}
