package stonering.enums.items.recipe;

import stonering.entity.item.Item;
import stonering.enums.items.ItemTagEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Determines ingredient for crafting. Is part of {@link Recipe}.
 * Items that have one of item types, ha
 *
 * @author Alexander on 05.01.2019.
 */
public class Ingredient {
    public final String key;                     // item/building part name, 'consumed', 'main'
    public final List<String> itemTypes;         // acceptable item types
    public final ItemTagEnum tag;                // acceptable item tags
    public final int quantity;                   // number of items

    public Ingredient(String key, List<String> itemTypes, ItemTagEnum tag, int quantity) {
        this.key = key;
        this.itemTypes = new ArrayList<>(itemTypes);
        this.tag = tag;
        this.quantity = quantity;
    }

    public boolean checkItem(Item item) {
        return item.tags.contains(tag) && itemTypes.contains(item.type.name);
    }
}
