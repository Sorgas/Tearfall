package stonering.enums;

import java.util.HashMap;

/**
 * Created by Alexander on 10.06.2017.
 */
public enum BlockTypesEnum {
    SPACE((byte) 0) {
    },

    WALL((byte) 1) {

    },
    FLOOR((byte) 2) {

    },
    STAIRS((byte) 3) {

    },
    RAMP((byte) 4) {

    };

    private byte code;
    private static HashMap<Byte, BlockTypesEnum> map;

    static {
        map = new HashMap<>();
        for (BlockTypesEnum type : BlockTypesEnum.values()) {
            map.put(type.code, type);
        }
    }

    BlockTypesEnum(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static BlockTypesEnum getType(byte code) {
        return map.get(code);
    }
}
