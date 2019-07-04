package stonering.entity.item.selectors;

import stonering.entity.item.Item;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public boolean check(List<Item> items) {
        for (int i = 0; i < items.size(); i++) {
            if (checkItem(items.get(i)))
                return true;
        }
        return false;
    }

    @Override
    public List<Item> selectItems(List<Item> items) {
        ArrayList<Item> selectedItems = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            if (checkItem(items.get(i))) {
                selectedItems.add(items.get(i));
                return selectedItems;
            }
        }
        return selectedItems;
    }

    private boolean checkItem(Item item) {
        return item.getTitle().equals(name);
    }

    @Override
    public String toString() {
        return "any " + materialTag + " " + name;
    }
}
