package stonering.enums.items;

import stonering.util.global.Logger;

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
    METAL(false), // brass(material) bar(type) // stones have no origin
    WOOD(false), // birch(material) log
    MEAT(false), // fox(origin) meat(material) piece(type)
    EDIBLE(false),
    RAW(true), // raw cow meat piece
    BOILABLE(false),
    ROASTABLE(false),
    BREWABLE(false),
    WATER(false),
    CLOTH(false),
    DEFAULT_TAG(false);

    private static boolean debug = true;
    private boolean displayable; // tags with true are displayed in items titles.

    TagEnum(boolean displayable) {
        this.displayable = displayable;
    }

    public static TagEnum get(String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            Logger.ITEMS.logError("Item tag with name: " + name + " not found.");
            return DEFAULT_TAG;
        }
    }

    public boolean isDisplayable() {
        return debug || displayable;
    }
}
