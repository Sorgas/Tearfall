package stonering.enums.blocks;

import java.util.HashMap;

/**
 * Created by Alexander on 10.06.2017.
 */
public enum BlockTypesEnum {
    SPACE(0),
    WALL(1),
    FLOOR(2),
    STAIRS(3),
    RAMP(4);

    private byte code;
    private static HashMap<Byte, BlockTypesEnum> map;

    static {
        map = new HashMap<>();
        for (BlockTypesEnum type : BlockTypesEnum.values()) {
            map.put(type.code, type);
        }
    }

    BlockTypesEnum(int code) {
        this.code = (byte) code;
    }

    public byte getCode() {
        return code;
    }

    public static BlockTypesEnum getType(byte code) {
        return map.get(code);
    }
}
