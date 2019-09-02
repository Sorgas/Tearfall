package stonering.entity.item.selectors;

import stonering.entity.item.Item;
import stonering.enums.items.TagEnum;
import stonering.enums.materials.MaterialMap;

/**
 * Selects any item with given type name and tag.
 *
 * @author Alexander on 25.06.2019.
 */
public class AnyMaterialTagItemSelector extends ItemSelector {
    private String name;         // item type name
    private TagEnum tag;

    public AnyMaterialTagItemSelector(String name, TagEnum tag) {
        this.name = name;
        this.tag = tag;
    }

    public boolean checkItem(Item item) {
        return item.getType().name.equals(name) && item.tags.contains(tag);
    }

    @Override
    public String toString() {
        return "any " + tag + " " + name;
    }
}
