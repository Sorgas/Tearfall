package stonering.enums.blocks;

import java.util.HashMap;

/**
 * Enum of all block types.
 *
 * @author Alexander Kuzyakov on 10.06.2017.
 */
public enum BlockTypesEnum {
    SPACE(0, 1), //not passable for walkers
    WALL(1, 0), // not passable
    FLOOR(2, 2), // passable
    RAMP(3, 2), // passable
    STAIRS(4, 2), // passable
    STAIRFLOOR(5, 2); // passable

    private byte code;
    private byte passing;
    private static HashMap<Byte, BlockTypesEnum> map;

    static {
        map = new HashMap<>();
        for (BlockTypesEnum type : BlockTypesEnum.values()) {
            map.put(type.code, type);
        }
    }

    BlockTypesEnum(int code, int passing) {
        this.code = (byte) code;
        this.passing = (byte) passing;
    }

    public byte getCode() {
        return code;
    }

    public byte getPassing() {
        return passing;
    }

    public static BlockTypesEnum getType(byte code) {
        return map.get(code);
    }
}
