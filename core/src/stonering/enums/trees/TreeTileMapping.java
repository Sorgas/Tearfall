package stonering.enums.trees;

import java.util.HashMap;

/**
 * Created by Alexander on 25.11.2017.
 */
public enum TreeTileMapping {
    TRUNK((byte) 12, (byte) 0),
    TRUNK_R((byte) 2, (byte) 1),
    TRUNK_L((byte) 3, (byte) 2),
    TRUNK_RL((byte) 4, (byte) 3),
    STOMP((byte) 10, (byte) 4),
    BRANCHES_R((byte) 13, (byte) 5),
    BRANCHES_L((byte) 7, (byte) 6),
    CROWN((byte) 14, (byte) 7);

    private byte code;
    private byte atlasX;
    private static HashMap<Byte, TreeTileMapping> map;

    static {
        map = new HashMap<>();
        for (TreeTileMapping type : TreeTileMapping.values()) {
            map.put(type.getCode(), type);
        }
    }

    TreeTileMapping(byte code, byte atlasX) {
        this.code = code;
        this.atlasX = atlasX;
    }

    public byte getCode() {
        return code;
    }

    public byte getAtlasX() {
        return atlasX;
    }

    public static TreeTileMapping getType(byte code) {
        return map.get(code);
    }

    public static TreeTileMapping getType(int code) {
        return map.get((byte) code);
    }
}
