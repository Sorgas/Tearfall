package stonering.enums.plants;

import java.util.HashMap;

/**
 * @author Alexander Kuzyakov on 30.10.2017.
 */
public enum TreeBlocksTypeEnum {
    STOMP(10), //treestomp
    ROOT(11),
    TRUNK(12),
    BRANCH(13),
    CROWN(14),
    SINGLE_PASSABLE(15),
    SINGLE_NON_PASSABLE(16);

    private int code;
    private static HashMap<Integer, TreeBlocksTypeEnum> map;

    static {
        map = new HashMap<>();
        for (TreeBlocksTypeEnum type : TreeBlocksTypeEnum.values()) {
            map.put(type.code, type);
        }
    }

    TreeBlocksTypeEnum(int code) {
        this.code = (byte) code;
    }

    public int getCode() {
        return code;
    }

    public static TreeBlocksTypeEnum getType(int code) {
        return map.get(code);
    }
}