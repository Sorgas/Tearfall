package stonering.enums.blocks;

import java.util.HashMap;

import static stonering.enums.blocks.BlockTypesEnum.PassageEnum.*;

/**
 * Enum of all block types.
 * Stairs give vertical passages. From STAIR walker can ascend to STAIR or DOWNSTAIRS, and return back respectively.
 * When blocks are dug, they leave number of products equal to difference between old and new block product property.
 *
 * @author Alexander Kuzyakov on 10.06.2017.
 */
public enum BlockTypesEnum {
    SPACE(0, IMPASSABLE, 5, false, 0, "space"), //not passable for walkers, liquids fall
    WALL(1, IMPASSABLE, 0, false, 3, "wall"), // not passable
    FLOOR(2, PASSABLE, 3, true, 1, "floor"), // passable, liquids don't fall
    STAIRS(3, PASSABLE, 2, false, 2, "stairs"), //DF-like stairs
    DOWNSTAIRS(4, PASSABLE, 4, true, 1, "downstairs"),
    RAMP(5, PASSABLE, 1, false, 2, "ramp"), // passable, liquids don't fall
    FARM(6, PASSABLE, 3, true, 1, "farm plot"); // passable

    public final byte CODE;
    public final PassageEnum PASSING;
    public final byte OPENNESS; // blocks with lower openness can be dug to higher ones
    public final int PRODUCT;
    // flat blocks rendered below other entities in a separate cycle
    public final boolean FLAT;
    public final String NAME; // name of constructions.

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

    BlockTypesEnum(int code, PassageEnum passing, int open, boolean flat, int product, String name) {
        CODE = (byte) code;
        PASSING = passing;
        OPENNESS = (byte) open;
        FLAT = flat;
        PRODUCT = product;
        NAME = name;
    }

    public static BlockTypesEnum getType(byte code) {
        return map.get(code);
    }

    public static BlockTypesEnum getType(String name) {
        return nameMap.get(name);
    }

    public enum PassageEnum {
        PASSABLE(1),
        IMPASSABLE(0);

        public byte VALUE;

        PassageEnum(int value) {
            VALUE = (byte) value;
        }
    }
}
