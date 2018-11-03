package stonering.entity.local.items.selectors;

import stonering.entity.local.items.Item;
import stonering.enums.materials.MaterialMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Selects 1 resource Item made of specified material.
 *
 * @author Alexander on 03.11.2018.
 */
public class ResourceItemSelector extends ItemSelector {
    private int material;

    public ResourceItemSelector(int material) {
        this.material = material;
    }

    public ResourceItemSelector(String materialName) {
        this.material = MaterialMap.getInstance().getId(materialName);
    }

    @Override
    public boolean check(List<Item> items) {
        for (Item item : items) {
            if (item.getMaterial() == material) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Item> selectItems(List<Item> items) {
        List<Item> list = new ArrayList<>();
        items.forEach(item -> {
            if (item.getMaterial() == material) {
                list.add(item);
            }
        });
        return items;
    }
}
