package stonering.enums.designations;

import java.util.HashMap;

/**
 * Created by Alexander on 27.12.2017.
 */
public enum DesignationsTypes {
    NONE(0, "none"),
    DIG(1, "digging wall"), // removes walls and ramps. leaves floor
    STAIRS(2, "cutting stairs"), //cuts stairs from wall.
    RAMP(3, "cutting ramp"), // digs ramp and upper cell.
    CHANNEL(4, "digging channel"); // digs cell and ramp on lower level

    private static HashMap<Byte, DesignationsTypes> map;
    private byte code;
    private String text;

    static {
        map = new HashMap<>();
        for (DesignationsTypes type : DesignationsTypes.values()) {
            map.put(type.code, type);
        }
    }

    DesignationsTypes(int code, String text) {
        this.code = (byte) code;
        this.text = text;
    }

    public byte getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public DesignationsTypes getType(int code) {
        return map.get(code);
    }
}
