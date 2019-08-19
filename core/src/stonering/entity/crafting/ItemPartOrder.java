package stonering.entity.crafting;

import stonering.entity.item.selectors.AnyMaterialTagItemSelector;
import stonering.entity.item.selectors.ItemSelector;
import stonering.enums.items.recipe.ItemPartRecipe;

/**
 * Part of {@link ItemOrder}.
 * On creation, accepts items of first type and any material from {@link ItemPartRecipe}.
 *
 * @author Alexander on 05.01.2019.
 */
public class ItemPartOrder {
    private String name; // produced item part, null for consumed ingredients
    private ItemOrder order;
    private ItemPartRecipe itemPartRecipe;
    private String selectedMaterial;
    private String selectedItemType;
    private ItemSelector itemSelector;

    public ItemPartOrder(ItemOrder order, String name) {
        this.order = order;
        this.name = name;
        itemPartRecipe = order.recipe.getItemPartRecipe(name);
        selectedMaterial = "any " + itemPartRecipe.materialTag;
        selectedItemType = itemPartRecipe.itemTypes.get(0);
    }

    /**
     * Updates item selector for this item part selector.
     */
    public void refreshSelector() {
        if(selectedMaterial.startsWith("any ")) {
            itemSelector = new AnyMaterialTagItemSelector(selectedItemType, itemPartRecipe.materialTag);
        }
    }

    public String getName() {
        return name;
    }

    public ItemOrder getOrder() {
        return order;
    }

    public ItemPartRecipe getItemPartRecipe() {
        return itemPartRecipe;
    }

    public String getSelectedMaterial() {
        return selectedMaterial;
    }

    public void setSelectedMaterial(String selectedMaterial) {
        this.selectedMaterial = selectedMaterial;
    }

    public String getSelectedItemType() {
        return selectedItemType;
    }

    public void setSelectedItemType(String selectedItemType) {
        this.selectedItemType = selectedItemType;
    }

    public ItemSelector getItemSelector() {
        return itemSelector;
    }
}
