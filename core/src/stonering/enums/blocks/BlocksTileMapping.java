package stonering.enums.blocks;

import java.util.HashMap;

/**
 * Mapping of block types code to sprite positions in atlas.
 *
 * @author Alexander Kuzyakov on 01.08.2017.
 */
public enum BlocksTileMapping {
    WALL(1, 0),
    FLOOR(2, 1),
    STAIRS(3, 2),
    STAIRFLOOR(4,3),

    RAMP_N(5, 4), // (0,1)
    RAMP_S(6, 5), // (0,-1)
    RAMP_W(7, 6), // (-1,0)
    RAMP_E(8, 7), // (1,0)

    RAMP_NW(9, 8),
    RAMP_NE(10, 9),
    RAMP_SW(11, 10),
    RAMP_SE(12, 11),

    RAMP_NWO(13, 12),
    RAMP_NEO(14, 13),
    RAMP_SWO(15, 14),
    RAMP_SEO(16, 15),
    FARM(17, 16),
;
    public final byte CODE;
    public final byte ATLAS_X;
    private static HashMap<Byte, BlocksTileMapping> map;

    static {
        map = new HashMap<>();
        for (BlocksTileMapping type : BlocksTileMapping.values()) {
            map.put(type.CODE, type);
        }
    }

    BlocksTileMapping(int code, int atlasX) {
        this.CODE = (byte) code;
        this.ATLAS_X = (byte) atlasX;
    }

    public static BlocksTileMapping getType(byte code) {
        return map.get(code);
    }
}