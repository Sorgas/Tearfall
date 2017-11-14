package stonering.enums.trees;

import java.util.HashMap;

/**
 * Created by Alexander on 30.10.2017.
 */
public enum TreeBlocksTypeEnum {
    STOMP((byte) 0) {
    },

    ROOT((byte) 1) {

    },
    TRUNK((byte) 2) {

    },
    BRANCH((byte) 3) {

    },
    CROWN((byte) 4) {

    };

    private byte code;
    private static HashMap<Byte, TreeBlocksTypeEnum> map;

    static {
        map = new HashMap<>();
        for (TreeBlocksTypeEnum type : TreeBlocksTypeEnum.values()) {
            map.put(type.code, type);
        }
    }

    TreeBlocksTypeEnum(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static TreeBlocksTypeEnum getType(byte code) {
        return map.get(code);
    }
}