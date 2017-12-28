package stonering.enums.designations;

import java.util.HashMap;

/**
 * Created by Alexander on 27.12.2017.
 */
public enum DesignationsTypes {
    DIG(1, 0, "digging");

    private static HashMap<Byte, DesignationsTypes> map;
    private byte code;
    private byte atlasX;
    private String text;

    static {
        map = new HashMap<>();
        for (DesignationsTypes type : DesignationsTypes.values()) {
            map.put(type.code, type);
        }
    }

    DesignationsTypes(int code, int atlasX, String text) {
        this.code = (byte) code;
        this.atlasX = (byte) atlasX;
        this.text = text;
    }

    public byte getCode() {
        return code;
    }

    public byte getAtlasX() {
        return atlasX;
    }

    public String getText() {
        return text;
    }

    public static byte getAtlasX(byte code) {
        return map.get(code).getAtlasX();
    }

    public DesignationsTypes getType(int code) {
        return map.get(code);
    }
}
