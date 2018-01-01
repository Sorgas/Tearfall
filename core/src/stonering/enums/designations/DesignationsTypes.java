package stonering.enums.designations;

import java.util.HashMap;

/**
 * Created by Alexander on 27.12.2017.
 */
public enum DesignationsTypes {
    NONE(0, -1, "none"),
    DIG(1, 0, "digging");

    private static HashMap<Byte, DesignationsTypes> map;
    private byte code;
    private String text;

    static {
        map = new HashMap<>();
        for (DesignationsTypes type : DesignationsTypes.values()) {
            map.put(type.code, type);
        }
    }

    DesignationsTypes(int code, int atlasX, String text) {
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
