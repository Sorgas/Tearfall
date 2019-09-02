package stonering.entity.crafting;

import stonering.entity.item.Item;
import stonering.entity.item.selectors.AnyMaterialTagItemSelector;
import stonering.entity.item.selectors.ItemSelector;
import stonering.enums.items.recipe.Ingredient;
import stonering.enums.materials.MaterialMap;
import stonering.game.GameMvc;
import stonering.game.model.lists.ItemContainer;
import stonering.util.global.Triple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Part of {@link ItemOrder}.
 * Tag and item type taken from recipe. Material and origin are set to any on creation and then can be changed by player.
 *
 * On creation, all items are observed, combined by material and type, and added to dropdown.
 *
 * //TODO add checkboxes for item types.
 * @author Alexander on 05.01.2019.
 */
public class IngredientOrder {
    public final ItemOrder order;
    public final Ingredient ingredient;

    public final List<String> itemTypes;
    private ItemSelector itemSelector;
    private String selectedMaterial;
    private String selectedOrigin;
    private String selectedItemType;

    public final Map<Triple<String, String, String>, Integer> options; // items, grouped by origin, material, type, to their quantity

    public IngredientOrder(ItemOrder order, Ingredient ingredient) {
        this.order = order;
        this.ingredient = ingredient;
        selectedMaterial = "any " + ingredient.tag;
        itemTypes = new ArrayList<>(ingredient.itemTypes);
        options = new HashMap<>();
    }

    /**
     * Collects all items with desired tags and types from map, filters unreachable ones,
     * combines them by origin, material, and type, and stores to list.
     */
    void updateOptions() {
        Triple<String, String, String> cacheTriple = new Triple<>("","", "");
        options.clear();
        for (Item item : GameMvc.instance().getModel().get(ItemContainer.class).getItemsForIngredient(ingredient)) {
            cacheTriple.set(item.getOrigin(), MaterialMap.instance().getMaterial(item.getMaterial()).getName(), item.getType().title);
            options.put(cacheTriple, options.getOrDefault(cacheTriple, 0));
        }
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
