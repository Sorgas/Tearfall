package stonering.enums.blocks;

import java.util.HashMap;

/**
 * Created by Alexander on 01.08.2017.
 */
public enum BlocksTileMapping {
    WALL((byte) 1, (byte) 0) {

    },
    FLOOR((byte) 2, (byte) 1) {

    },
    STAIRS((byte) 3, (byte) 14) {

    },
    RAMP_NW((byte) 4, (byte) 2) {

    },
    RAMP_NE((byte) 5, (byte) 3) {

    },
    RAMP_SW((byte) 6, (byte) 4) {

    },
    RAMP_SE((byte) 7, (byte) 5) {

    },
    RAMP_N2((byte) 8, (byte) 6) {

    },
    RAMP_S2((byte) 9, (byte) 7) {

    },
    RAMP_W2((byte) 10, (byte) 8) {

    },
    RAMP_E2((byte) 11, (byte) 9) {

    },
    RAMP_N((byte) 12, (byte) 10) {

    },
    RAMP_S((byte) 13, (byte) 11) {

    },
    RAMP_W((byte) 14, (byte) 12) {

    },
    RAMP_E((byte) 15, (byte) 13) {

    };

    private byte code;
    private byte atlasX;
    private static HashMap<Byte, BlocksTileMapping> map;

    static {
        map = new HashMap<>();
        for (BlocksTileMapping type : BlocksTileMapping.values()) {
            map.put(type.code, type);
        }
    }

    BlocksTileMapping(byte code, byte atlasX) {
        this.code = code;
        this.atlasX = atlasX;
    }

    public byte getCode() {
        return code;
    }

    public byte getAtlasX() {
        return atlasX;
    }

    public static BlocksTileMapping getType(byte code) {
        return map.get(code);
    }
}