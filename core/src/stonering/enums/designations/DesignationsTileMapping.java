package stonering.enums.designations;

import java.util.HashMap;

/**
 * Created by Alexander on 27.12.2017.
 */
public enum DesignationsTileMapping {
    DIG((byte) 1, (byte) 0);

    private byte code;
    private byte atlasX;
    private static HashMap<Byte, DesignationsTileMapping> map;

    static {
        map = new HashMap<>();
        for (DesignationsTileMapping type : DesignationsTileMapping.values()) {
            map.put(type.code, type);
        }
    }

    DesignationsTileMapping(byte code, byte atlasX) {
        this.code = code;
        this.atlasX = atlasX;
    }

    public byte getCode() {
        return code;
    }

    public byte getAtlasX() {
        return atlasX;
    }

    public static DesignationsTileMapping getType(byte code) {
        return map.get(code);
    }
}
