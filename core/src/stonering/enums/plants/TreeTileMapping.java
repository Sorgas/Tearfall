package stonering.enums.plants;

import java.util.HashMap;

/**
 * Stores atlas x for different parts of a tree.
 * Atlas Y is determined by specimen and lifestage
 *
 * @author Alexander Kuzyakov on 25.11.2017.
 */
public enum TreeTileMapping {
    TRUNK(12, 0),
    TRUNK_R(2, 1),
    TRUNK_L(3, 2),
    TRUNK_RL(4, 3),
    STOMP(10, 4),
    BRANCHES_R(13, 5),
    BRANCHES_L(7, 6),
    CROWN(14, 7),
    ROOT(11, 8);

    private byte code;
    private byte atlasX;
    private static HashMap<Byte, TreeTileMapping> map;

    static {
        map = new HashMap<>();
        for (TreeTileMapping type : TreeTileMapping.values()) {
            map.put(type.getCode(), type);
        }
    }

    TreeTileMapping(int code, int atlasX) {
        this.code = (byte) code;
        this.atlasX = (byte) atlasX;
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
