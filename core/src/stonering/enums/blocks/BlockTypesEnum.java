package stonering.enums.blocks;

import java.util.HashMap;

/**
 * Enum of all block types.
 *
 * @author Alexander Kuzyakov on 10.06.2017.
 */
public enum BlockTypesEnum {
    SPACE(0, 1), //not passable for walkers, liquids fall
    WALL(1, 0), // not passable
    FLOOR(2, 2), // passable, liquids don't fall
    RAMP(3, 2), // passable, liquids don't fall

    // leads 1 level up if there is STAIRS or STAIRFLOOR. leads 1 level down, if there is STAIRS.
    STAIRS(4, 2), // passable, liquids don't fall if lower block is not stairs

    // automatically placed when stairs constructed, turns to SPACE when stairs removed
    STAIRFLOOR(5, 2); // passable, liquids fall

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
        this.passing = (byte) passing; //0: none, 1: fliers only, 2: walkers & fliers
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
