package stonering.enums.items;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of all tags for items.
 *
 * @author Alexander on 02.09.2019.
 */
public enum TagEnum {
    STONE(false), // gabbro(material) stone(type) // stones have no origin
    STONE_EXTRUSIVE(false), // used for stone layers generation
    STONE_INTRUSIVE(false),
    STONE_SEDIMENTARY(false),
    METAL(false), // brass(material) bar(type) // bars have no origin
    WOOD(false), // birch(material) log
    MEAT(false), // fox(origin) meat(material) piece(type)
    EDIBLE(false),
    RAW(true), // raw cow meat piece
    BOILABLE(false),
    ROASTABLE(false),
    BREWABLE(false),
    WATER(false),
    CLOTH(false),
    CRAFTING_MATERIAL(false),
    DEFAULT_TAG(false);

    private static boolean debug = true;
    private boolean displayable; // tags with true are displayed in items titles.
    private static Map<String, TagEnum> map = new HashMap<>();

    static {
        for (TagEnum tag : values()) {
            map.put(tag.name().toLowerCase(), tag);
        }
    }

    TagEnum(boolean displayable) {
        this.displayable = displayable;
    }

    public static TagEnum get(String name) {
        return map.getOrDefault(name, DEFAULT_TAG);
    }

    public static boolean isTag(String name) {
        return map.containsKey(name);
    }

    public boolean isDisplayable() {
        return debug || displayable;
    }
}
