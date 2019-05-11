package stonering.enums.plants;

import java.util.HashMap;

/**
 * Enumeration of plant block types. Determines passage through blocks, and harvest/cut prodcuts.
 *
 * @author Alexander Kuzyakov on 30.10.2017.
 *
 * //TODO climbing on trees, walking on branches.
 */
public enum PlantBlocksTypeEnum {
    STOMP(10, false, "log"),
    ROOT(11, false, "root"),
    TRUNK(12, false, "log"),
    BRANCH(13, true, "branch"),
    CROWN(14, true, null),
    SINGLE_PASSABLE(15, true, null),
    SINGLE_NON_PASSABLE(16, false, null);

    public final int code;
    public final boolean passable;
    public final String cutProduct;
    private static HashMap<Integer, PlantBlocksTypeEnum> map;

    static {
        map = new HashMap<>();
        for (PlantBlocksTypeEnum type : PlantBlocksTypeEnum.values()) {
            map.put(type.code, type);
        }
    }

    PlantBlocksTypeEnum(int code, boolean passable, String cutProduct) {
        this.code = (byte) code;
        this.passable = passable;
        this.cutProduct = cutProduct;
    }

    public int getCode() {
        return code;
    }

    public static PlantBlocksTypeEnum getType(int code) {
        return map.get(code);
    }
}