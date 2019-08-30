package stonering.entity.crafting;

import stonering.entity.item.selectors.AnyMaterialTagItemSelector;
import stonering.entity.item.selectors.ItemSelector;
import stonering.enums.items.recipe.Ingredient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Part of {@link ItemOrder}.
 * Tag and item type taken from recipe. Material is set to any on creation and then can be changed by player.
 * On creation, all items are observed, combined by material and type, and added to dropdown.
 *
 * @author Alexander on 05.01.2019.
 */
public class IngredientOrder {
    public final ItemOrder order;
    public final Ingredient ingredient;
    private HashMap<String, List<String>> options; // item type to materials

    public final List<String> itemType;
    private ItemSelector itemSelector;
    private String selectedMaterial;
    private String selectedItemType;

    public IngredientOrder(ItemOrder order, Ingredient ingredient) {
        this.order = order;
        this.ingredient = ingredient;
        selectedMaterial = "any " + ingredient.tag;
        itemType = new ArrayList<>(ingredient.itemTypes);
        options = new HashMap<>();
    }

    void updateOptions() {

    }

    /**
     * Updates item selector for this item part selector.
     */
    public void refreshSelector() {
        if(selectedMaterial.startsWith("any ")) {
            itemSelector = new AnyMaterialTagItemSelector(selectedItemType, ingredient.tag);
        }
    }

    public String getSelectedMaterial() {
        return selectedMaterial;
    }

    public void setSelectedMaterial(String selectedMaterial) {
        this.selectedMaterial = selectedMaterial;
    }

    public ItemSelector getItemSelector() {
        return itemSelector;
    }
}
