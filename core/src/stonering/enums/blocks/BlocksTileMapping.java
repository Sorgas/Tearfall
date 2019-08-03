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
    STAIRS(3, 14), //TODO move STAIRS next to FLOOR, add STAIR_FLOOR and FARM, update assets

    RAMP_N(3, 2), // (0,1)
    RAMP_S(4, 3), // (0,-1)
    RAMP_W(5, 4), // (-1,0)
    RAMP_E(6, 5), // (1,0)

    RAMP_NW(7, 6),
    RAMP_NE(8, 7),
    RAMP_SW(9, 8),
    RAMP_SE(10, 9),

    RAMP_NWO(11, 10),
    RAMP_NEO(12, 11),
    RAMP_SWO(13, 12),
    RAMP_SEO(14, 13),;
//    FARM();

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