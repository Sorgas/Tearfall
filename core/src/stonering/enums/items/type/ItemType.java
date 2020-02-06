package stonering.enums.items.type;

import stonering.entity.Entity;
import stonering.enums.items.type.raw.RawItemType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Descriptor class of item. Stores all aspects, valid to the whole type of item, not for specific ones.
 * (e.g. wear, fuel, but not material, condition, ownership)
 */
public class ItemType extends Entity implements Cloneable {
    public final String name;                                // id
    public final String title;                               // displayable name
    public final String description;                         // displayable description
    public final ToolItemType tool;                          // is set if this item could be used as tool TODO replace with type aspect
    public Map<String, List<String>> itemAspects;           // other aspects, item aspects filled from this on generation.
    public int[] atlasXY;
    public String color;

    public ItemType(RawItemType rawType) {
        super();
        name = rawType.name;
        title = rawType.title.isEmpty() ? capitalize(rawType.name) : rawType.title;
        description = rawType.description;
        tool = rawType.tool;
        atlasXY = rawType.atlasXY;
        itemAspects = new HashMap<>();
    }

    /**
     * Extends given type with values from raw type.
     */
    public ItemType(ItemType type, RawItemType rawType, String namePrefix) {
        super();
        name = namePrefix + type.name;
        title = rawType.title.isEmpty() ? capitalize(name) : rawType.title;
        description = rawType.description != null ? rawType.description : type.description;
        tool = rawType.tool != null ? rawType.tool : type.tool;
        itemAspects = new HashMap<>(type.itemAspects); // TODO clone aspects
        atlasXY = rawType.atlasXY != null ? rawType.atlasXY.clone() : type.atlasXY.clone();
        color = rawType.color != null ? rawType.color : type.color;
    }

    private String capitalize(String text) {
        return (text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase()).replace('_', ' ');
    }
}
