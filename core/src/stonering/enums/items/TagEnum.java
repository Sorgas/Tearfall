package stonering.enums.items;

import stonering.util.global.Logger;

/**
 * Enumeration of all tags for items.
 *
 * @author Alexander on 02.09.2019.
 */
public enum TagEnum {
    STONE,
    WOOD,
    MEAT,
    EDIBLE,
    RAW,
    BOILABLE,
    ROASTABLE,
    DEFAULT;

    public static TagEnum get(String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch(IllegalArgumentException e) {
            Logger.ITEMS.logError("Item tag with name: " + name + " not found.");
            return DEFAULT;
        }
    }
}
