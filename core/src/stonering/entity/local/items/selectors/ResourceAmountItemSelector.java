package stonering.entity.local.items.selectors;

import stonering.entity.local.items.Item;
import stonering.enums.materials.MaterialMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Selects amount of resource items.
 *
 * @author Alexander on 27.10.2018.
 */
public class ResourceAmountItemSelector extends ItemSelector {
    private int amount; //sm^3
    private String itemType;
    private String materialType; // material class (metal, wood, etc)
    private int material; // material id. -1 if materialType is used.

    /**
     * Creates selector of any material of type.
     *
     * @param amount
     * @param itemType
     * @param materialType
     */
    public ResourceAmountItemSelector(int amount, String itemType, String materialType) {
        this.amount = amount;
        this.itemType = itemType;
        this.materialType = materialType;
        material = -1;
    }

    /**
     * Creates selector of specific meterial.
     *
     * @param amount
     * @param itemType
     * @param material
     */
    public ResourceAmountItemSelector(int amount, String itemType, int material) {
        this.amount = amount;
        this.itemType = itemType;
        this.material = material;
    }

    @Override
    public boolean check(List<Item> items) {
        return selectItems(items) != null;
    }

    @Override
    public List<Item> selectItems(List<Item> items) {
        HashMap<Integer, ItemsWithAmount> itemGroups = new HashMap<>(); // material to item group
        for (Item item : items) {
            if (checkItem(item)) {
                int itemMaterial = item.getMaterial();
                if (!itemGroups.keySet().contains(itemMaterial)) { // create new group if needed
                    itemGroups.put(itemMaterial, new ItemsWithAmount());
                }
                itemGroups.get(itemMaterial).addItem(item); // add new item to group
                if (itemGroups.get(itemMaterial).totalAmount >= amount) { // return group if full
                    return itemGroups.get(itemMaterial).items;
                }
            }
        }
        return null;
    }

    private boolean checkItem(Item item) {
        int itemMaterial = item.getMaterial();
        if (item.getType().isResource() && item.getType().getTitle().equals(itemType)) {
            if (material != -1) {
                return itemMaterial == material;
            } else {
                return MaterialMap.getInstance().getMaterial(itemMaterial).getTypes().contains(materialType);
            }
        }
        return false;
    }

    /**
     * Stores total volume of listed items.
     * Does not checks materials or duplicates.
     */
    private class ItemsWithAmount {
        List<Item> items = new ArrayList<>();
        int totalAmount = 0;

        private void addItem(Item item) {
            items.add(item);
//            totalAmount += item.getMainPart_().getVolume();
            totalAmount++;
        }
    }
}
