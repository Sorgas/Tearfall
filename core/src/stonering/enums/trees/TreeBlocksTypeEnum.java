package stonering.enums.trees;

import java.util.HashMap;

/**
 * Created by Alexander on 30.10.2017.
 */
public enum TreeBlocksTypeEnum {
    STOMP(10),
    ROOT(11),
    TRUNK(12),
    BRANCH(13),
    CROWN(14);

    private byte code;
    private static HashMap<Byte, TreeBlocksTypeEnum> map;

    static {
        map = new HashMap<>();
        for (TreeBlocksTypeEnum type : TreeBlocksTypeEnum.values()) {
            map.put(type.code, type);
        }
    }

    TreeBlocksTypeEnum(int code) {
        this.code = (byte) code;
    }

    public byte getCode() {
        return code;
    }

    public static TreeBlocksTypeEnum getType(byte code) {
        return map.get(code);
    }
}