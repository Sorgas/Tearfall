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
    public final ItemOrder order;
    public final ItemPartRecipe partRecipe;

    private String selectedMaterial;
    private String selectedItemType;
    private ItemSelector itemSelector;

    public ItemPartOrder(ItemOrder order, ItemPartRecipe partRecipe) {
        this.order = order;
        this.partRecipe = partRecipe;
        selectedMaterial = "any " + partRecipe.materialTag;
        selectedItemType = partRecipe.itemTypes.get(0);
    }

    /**
     * Updates item selector for this item part selector.
     */
    public void refreshSelector() {
        if(selectedMaterial.startsWith("any ")) {
            itemSelector = new AnyMaterialTagItemSelector(selectedItemType, partRecipe.materialTag);
        }
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
