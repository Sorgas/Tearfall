package stonering.enums.blocks;

import java.util.HashMap;

import static stonering.enums.blocks.PassageEnum.*;

/**
 * Enum of all block types.
 * Stairs give vertical passages. From STAIR walker can ascend to STAIR or DOWNSTAIRS, and return back respectively.
 * When blocks are dug, they leave number of products equal to difference between old and new block product property.
 *
 * @author Alexander Kuzyakov on 10.06.2017.
 */
public enum BlockTypeEnum {
    SPACE(0, IMPASSABLE, 16, true, 0, true, "space"), //not passable for walkers, liquids fall
    WALL(1, IMPASSABLE, 0, false, 3, false, "wall"), // not passable
    FLOOR(2, PASSABLE, 12, true, 1, false, "floor"), // passable, liquids don't fall
    STAIRS(3, PASSABLE, 8, false, 2, false, "stairs"), // DF-like stairs
    DOWNSTAIRS(4, PASSABLE, 14, true, 1, true, "downstairs"),
    RAMP(5, PASSABLE, 6, false, 2, false, "ramp"), // passable, liquids don't fall
    FARM(17, PASSABLE, 12, true, 1, false, "farm plot"); // passable

    public final byte CODE; // stored on map
    public final PassageEnum PASSING;
    public final byte OPENNESS; // blocks with lower openness can be dug to higher ones
    public final int PRODUCT;
    // flat blocks rendered below other entities in a separate cycle
    public final boolean FLAT;
    public final boolean PASS_LIQUID_DOWN;
    public final String NAME; // name of constructions.

    private static HashMap<Byte, BlockTypeEnum> map;
    private static HashMap<String, BlockTypeEnum> nameMap;

    static {
        map = new HashMap<>();
        for (BlockTypeEnum type : BlockTypeEnum.values()) {
            map.put(type.CODE, type);
        }
        nameMap = new HashMap<>();
        for (BlockTypeEnum type : BlockTypeEnum.values()) {
            nameMap.put(type.NAME, type);
        }
    }

    BlockTypeEnum(int code, PassageEnum passing, int open, boolean flat, int product, boolean passLiquidDown, String name) {
        CODE = (byte) code;
        PASSING = passing;
        OPENNESS = (byte) open;
        FLAT = flat;
        PRODUCT = product;
        PASS_LIQUID_DOWN = passLiquidDown;
        NAME = name;
    }

    public static BlockTypeEnum getType(byte code) {
        return map.get(code);
    }

    public static BlockTypeEnum getType(String name) {
        return nameMap.get(name);
    }
}
