package stonering.entity.item.selectors;

import stonering.entity.item.Item;
import stonering.enums.items.ItemTagEnum;

/**
 * Selects any item with given type name and tag.
 *
 * @author Alexander on 25.06.2019.
 */
public class AnyMaterialTagItemSelector extends ItemSelector {
    private String name;         // item type name
    private ItemTagEnum tag;

    public AnyMaterialTagItemSelector(String name, ItemTagEnum tag) {
        this.name = name;
        this.tag = tag;
    }

    public boolean checkItem(Item item) {
        return item.type.name.equals(name) && item.tags.contains(tag);
    }

    @Override
    public String toString() {
        return "any " + tag + " " + name;
    }
}
