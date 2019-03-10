package stonering.enums.plants;

import java.util.HashMap;

/**
 * @author Alexander Kuzyakov on 30.10.2017.
 *
 * //TODO climbing on trees, walking on branches.
 */
public enum PlantBlocksTypeEnum {
    STOMP(10, false), //treestomp
    ROOT(11, false),
    TRUNK(12, false),
    BRANCH(13, true),
    CROWN(14, true),
    SINGLE_PASSABLE(15, true),
    SINGLE_NON_PASSABLE(16, false);

    private int code;
    public final boolean passable;
    private static HashMap<Integer, PlantBlocksTypeEnum> map;

    static {
        map = new HashMap<>();
        for (PlantBlocksTypeEnum type : PlantBlocksTypeEnum.values()) {
            map.put(type.code, type);
        }
    }

    PlantBlocksTypeEnum(int code, boolean passable) {
        this.code = (byte) code;
        this.passable = passable;
    }

    public int getCode() {
        return code;
    }

    public static PlantBlocksTypeEnum getType(int code) {
        return map.get(code);
    }
}