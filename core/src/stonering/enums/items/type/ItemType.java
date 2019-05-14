package stonering.enums.items.type;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Descriptor class of item. Stores all aspects, valid to the whole type of items, not for specific ones.
 * (e.g. not material, condition, ownership)
 */
public class ItemType {
    public String name;                                // id
    public String title;                               // displayable title
    public String description;                         // displayable description
    public float valueMod;                             // trading price modifier
    public boolean isResource = false;                 // true if this item can be used for crafting as raw resource (its volume is counted).

    public WearItemType wear;                          // is set if this item could be worn
    public ToolItemType tool;                          // is set if this item could be used as tool
    public ContainerItemType container;                // is set if this item could contain other items

    public ArrayList<ItemPartType> parts;              // defines parts of items. first one is main

    public HashMap<String, ArrayList<Object>> aspects; // other aspects, item aspects filled from this on generation.

    // render
    public int[] atlasXY;
    public String color;

    public ItemType() {
        parts = new ArrayList<>();
        aspects = new HashMap<>();
    }
}
