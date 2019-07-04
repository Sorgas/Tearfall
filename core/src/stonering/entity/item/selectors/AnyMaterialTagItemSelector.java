package stonering.entity.item.selectors;

import stonering.entity.item.Item;
import stonering.enums.materials.MaterialMap;

/**
 * Selects any item with given name and material tag.
 *
 * @author Alexander on 25.06.2019.
 */
public class AnyMaterialTagItemSelector extends ItemSelector {
    private String name;         // item type name
    private String materialTag;

    public AnyMaterialTagItemSelector(String name, String materialTag) {
        this.name = name;
        this.materialTag = materialTag;
    }

    public boolean checkItem(Item item) {
        return item.getType().name.equals(name) &&
                MaterialMap.instance().getMaterial(item.getMaterial()).getTags().contains(materialTag);
    }

    @Override
    public String toString() {
        return "any " + materialTag + " " + name;
    }
}
