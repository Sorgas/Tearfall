package stonering.enums;

import java.util.HashMap;

/**
 * Created by Alexander on 01.08.2017.
 */
public enum BlocksTileMapping {
    WALL((byte) 0, 0) {

    },
    FLOOR((byte) 1, 1) {

    },
    RAMP_NW((byte) 2, 2) {

    },
    RAMP_NE((byte) 3, 3) {

    },
    RAMP_SW((byte) 4, 4) {

    },
    RAMP_SE((byte) 5, 5) {

    },
    RAMP_N2((byte) 6, 6) {

    },
    RAMP_S2((byte) 7, 7) {

    },
    RAMP_W2((byte) 8, 8) {

    },
    RAMP_E2((byte) 9, 9) {

    },
    RAMP_N((byte) 10, 10) {

    },
    RAMP_S((byte) 11, 11) {

    },
    RAMP_W((byte) 12, 12) {

    },
    RAMP_E((byte) 13, 13) {

    },
    STAIRS((byte) 14, 14) {

    };

    private byte code;
    private int atlasX;
    private static HashMap<Byte, BlocksTileMapping> map;

    static {
        for (BlocksTileMapping type : BlocksTileMapping.values()) {
            map.put(type.code, type);
        }
    }

    BlocksTileMapping(byte code, int atlasX) {
        this.code = code;
        this.atlasX = atlasX;
    }

    public byte getCode() {
        return code;
    }

    public int getAtlasX() {
        return atlasX;
    }

    public static BlocksTileMapping getType(byte code) {
        return map.get(code);
    }
}