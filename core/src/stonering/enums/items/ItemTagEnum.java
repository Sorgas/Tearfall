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
    STONE(false), // gabbro(material) rock(type) // stones have no origin
    STONE_EXTRUSIVE(false), // used for stone layers generation
    STONE_INTRUSIVE(false),
    STONE_SEDIMENTARY(false),
    METAL(false), // brass(material) bar(type)
    WOOD(false), // birch(material) log(type)
    MEAT(false), // fox(origin) meat(material) piece(type)
    ORE(false), // magnetite(material) rock(type)

    COOKABLE(false), // can be boiled or roasted
    EDIBLE(false), // can be eaten
    DRINKABLE(false), // can be drunk
    RAW(true), // raw cow meat piece,
    SPOILED(true), // spoiled raw cow meat peace
    PREPARED(true), // cow meat stew

    BREWABLE(false), // item can be prepared into drink

    WATER(false),
    CLOTH(false),
    MATERIAL(false); // item is raw material for building and crafting

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

    public static ItemTagEnum get(String name) {
        return map.get(name);
    }

    public boolean isDisplayable() {
        return debug || displayable;
    }
}
