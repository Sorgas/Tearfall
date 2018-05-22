package stonering.enums.designations;

import java.util.HashMap;

/**
 * Mapping of designation demarcation to ins sprites positions in atlas.
 *
 * Created by Alexander on 27.12.2017.
 */
public enum DesignationsTileMapping {
    DIG((byte) 1, (byte) 0),
    STAIRS((byte) 2, (byte) 1),
    RAMP((byte) 3, (byte) 2),
    CHANNEL((byte) 4,(byte) 3);

    private byte code;
    private byte atlasX;
    private static HashMap<Byte, Byte> map;

    static {
        map = new HashMap<>();
        for (DesignationsTileMapping type : DesignationsTileMapping.values()) {
            map.put(type.code, type.atlasX);
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

    public static byte getAtlasX(byte code) {
        return map.get(code);
    }
}
