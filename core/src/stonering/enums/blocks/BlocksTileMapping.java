package stonering.enums.blocks;

import java.util.HashMap;

/**
 * Mapping of block types code to sprite positions in atlas.
 *
 * @author Alexander Kuzyakov on 01.08.2017.
 */
public enum BlocksTileMapping {
    WALL((byte) 1, (byte) 0),
    FLOOR((byte) 2, (byte) 1),
    STAIRS((byte) 3, (byte) 14),

    RAMP_N((byte) 3, (byte) 2),//(0,1)
    RAMP_S((byte) 4, (byte) 3),//(0,-1)
    RAMP_W((byte) 5, (byte) 4),//(-1,0)
    RAMP_E((byte) 6, (byte) 5),//(1,0)

    RAMP_NW((byte) 7, (byte) 6),
    RAMP_NE((byte) 8, (byte) 7),
    RAMP_SW((byte) 9, (byte) 8),
    RAMP_SE((byte) 10, (byte) 9),

    RAMP_NWO((byte) 11, (byte) 10),
    RAMP_NEO((byte) 12, (byte) 11),
    RAMP_SWO((byte) 13, (byte) 12),
    RAMP_SEO((byte) 14, (byte) 13);

    public final byte CODE;
    public final byte ATLAS_X;
    private static HashMap<Byte, BlocksTileMapping> map;

    static {
        map = new HashMap<>();
        for (BlocksTileMapping type : BlocksTileMapping.values()) {
            map.put(type.CODE, type);
        }
    }

    BlocksTileMapping(byte code, byte atlasX) {
        this.CODE = code;
        this.ATLAS_X = atlasX;
    }

    public static BlocksTileMapping getType(byte code) {
        return map.get(code);
    }
}