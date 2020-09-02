package stonering.enums.items.type;

import stonering.entity.Entity;
import stonering.enums.items.ItemTagEnum;
import stonering.enums.items.type.raw.RawItemType;

import java.util.*;

/**
 * Descriptor class of item. Stores all aspects, valid to the whole type of item, not for specific ones.
 * (e.g. wear, fuel, but not material, condition, ownership)
 * //TODO add icons to item types and type tags, to be used in crafting ui
 */
public class ItemType extends Entity implements Cloneable {
    public final String name;                                // id
    public final String title;                               // displayable name
    public final String description;                         // displayable description
    public final ToolItemType tool;                          // is set if this item could be used as tool TODO replace with type aspect
    public Map<String, List<String>> itemAspects;            // other aspects, item aspects filled from this on generation.
    public int[] atlasXY;
    public String color;
    public Set<ItemTagEnum> tags;
    public List<String> requiredParts;
    public List<String> optionalParts;
    public String atlasName;

    public ItemType(RawItemType rawType) {
        super();
        name = rawType.name;
        title = rawType.title.isEmpty() ? rawType.name : rawType.title;
        description = rawType.description;
        tool = rawType.tool;
        atlasXY = rawType.atlasXY;
        itemAspects = new HashMap<>();
        tags = new HashSet<>();
        requiredParts = new ArrayList<>();
        if(rawType.requiredParts.isEmpty()) {
            requiredParts.add(name);
        } else {
            requiredParts.addAll(rawType.requiredParts);
        }
        optionalParts = new ArrayList<>(rawType.optionalParts);
        rawType.tags.stream()
                .map(ItemTagEnum::get)
                .filter(Objects::nonNull)
                .forEach(tags::add);
    }

    /**
     * Extends given type with values from raw type.
     */
    public ItemType(ItemType type, RawItemType rawType, String namePrefix) {
        super();
        name = namePrefix + type.name;
        title = rawType.title.isEmpty() ? name : rawType.title;
        description = rawType.description != null ? rawType.description : type.description;
        tool = rawType.tool != null ? rawType.tool : type.tool;
        itemAspects = new HashMap<>(type.itemAspects); // TODO clone aspects
        atlasXY = rawType.atlasXY != null ? rawType.atlasXY.clone() : type.atlasXY.clone();
        color = rawType.color != null ? rawType.color : type.color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemType itemType = (ItemType) o;
        return name.equals(itemType.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
