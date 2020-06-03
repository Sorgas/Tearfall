package stonering.enums.items;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of all tags for items.
 * Food tags - food tags influence, which food item will be taken for eating.
 * RAW - gives penalty on eating. Lower selection priority.
 * SPOILED - gives penalty on eating. Lower selection priority.
 * PREPARED - gives bonus on eating. Increased selection priority.
 * Fruits and vegetables have no RAW tag and no penalties on eating.
 *
 * @author Alexander on 02.09.2019.
 */
public enum ItemTagEnum {
    STONE, // gabbro(material) rock(type) // stones have no origin
    STONE_EXTRUSIVE, // used for stone layers generation
    STONE_INTRUSIVE,
    STONE_SEDIMENTARY,
    METAL, // brass(material) bar(type)
    WOOD, // birch(material) log(type)
    MEAT, // fox(origin) meat(material) piece(type)
    ORE, // magnetite(material) rock(type)

    COOKABLE, // can be boiled or roasted
    EDIBLE, // can be eaten
    DRINKABLE, // can be drunk
    RAW(true), // raw cow meat piece,
    SPOILED(true), // spoiled raw cow meat peace
    PREPARED(true), // cow meat stew

    BREWABLE, // item can be prepared into drink

    WATER,
    CLOTH,
    MATERIAL; // item is raw material for building and crafting

    private static boolean debug = false;
    private boolean displayable; // tags with true are displayed in items titles.
    private static Map<String, ItemTagEnum> map = new HashMap<>();

    static {
        for (ItemTagEnum tag : values()) {
            map.put(tag.name().toLowerCase(), tag);
        }
    }

    ItemTagEnum(boolean displayable) {
        this.displayable = displayable;
    }

    ItemTagEnum() {
        this(false);
    }

    public static ItemTagEnum get(String name) {
        return map.get(name);
    }

    public boolean isDisplayable() {
        return debug || displayable;
    }
}
