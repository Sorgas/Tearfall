package stonering.enums.blocks;

import java.util.HashMap;

/**
 * Enum of all block types.
 * Stairs give vertical passages. From STAIR walker can ascend to STAIR or DOWNSTAIRS, and return back respectively.
 *
 * @author Alexander Kuzyakov on 10.06.2017.
 */
public enum BlockTypesEnum {
    SPACE(0, 1, 6, "space"), //not passable for walkers, liquids fall
    WALL(1, 0, 0, "wall"), // not passable
    FLOOR(2, 2, 4, "floor"), // passable, liquids don't fall
    STAIRS(3, 2, 3, "stairs"), //DF-like stairs
    DOWNSTAIRS(4, 2, 5, "downstairs"),
    UPSTAIRS(4, 2, 2, "upstairs"),
    RAMP(5, 2, 1, "ramp"), // passable, liquids don't fall
    FARM(6, 2, 4, "farm plot"); // passable

    public final byte CODE;
    public final byte PASSING;
    public final byte OPENNESS; // blocks with lower openness can be dug to higher ones
    public final String NAME; // name of constructions.

    // for use outside of enum
    public static final byte NOT_PASSABLE = 0;
    public static final byte FLY_ONLY_PASSABLE = 1;
    public static final byte PASSABLE = 2;

    private static HashMap<Byte, BlockTypesEnum> map;
    private static HashMap<String, BlockTypesEnum> nameMap;

    static {
        map = new HashMap<>();
        for (BlockTypesEnum type : BlockTypesEnum.values()) {
            map.put(type.CODE, type);
        }
        nameMap = new HashMap<>();
        for (BlockTypesEnum type : BlockTypesEnum.values()) {
            nameMap.put(type.NAME, type);
        }
    }

    BlockTypesEnum(int code, int passing, int open, String name) {
        this.CODE = (byte) code;
        this.PASSING = (byte) passing; //0: none, 1: fliers only, 2: walkers & fliers
        this.OPENNESS = (byte) open;
        this.NAME = name;
    }

    public static BlockTypesEnum getType(byte code) {
        return map.get(code);
    }

    public static BlockTypesEnum getType(String name) {
        return nameMap.get(name);
    }

    public static boolean hasType(String name) {
        return nameMap.containsKey(name);
    }
}
