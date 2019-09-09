package stonering.enums.items.type;

import stonering.entity.Entity;
import stonering.entity.item.aspects.FuelAspect;
import stonering.entity.item.aspects.ValueAspect;
import stonering.enums.items.type.raw.RawItemType;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Descriptor class of item. Stores all aspects, valid to the whole type of item, not for specific ones.
 * (e.g. wear, fuel, but not material, condition, ownership)
 */
public class ItemType extends Entity {
    public final String name;                                // id
    public final String title;                               // displayable name
    public final String description;                         // displayable description
    public final ToolItemType tool;                          // is set if this item could be used as tool
    public final List<ItemPartType> parts;              // defines parts of item. first one is main
    public Map<String, List<String>> aspects;           // other aspects, item aspects filled from this on generation.

    // render
    public int[] atlasXY;
    public String color;

    public ItemType(RawItemType rawItemType) {
        super();
        name = rawItemType.name;
        title = rawItemType.title.isEmpty() ? capitalize(rawItemType.name) : rawItemType.title;
        description = rawItemType.description;
        tool = rawItemType.tool;
        atlasXY = rawItemType.atlasXY;
        parts = new ArrayList<>();
        aspects = new HashMap<>();
    }

    private String capitalize(String text) {
        return (text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase()).replace('_', ' ');
    }
}
