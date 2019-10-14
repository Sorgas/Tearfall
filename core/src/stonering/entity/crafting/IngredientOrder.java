package stonering.entity.crafting;

import stonering.entity.item.Item;
import stonering.entity.item.selectors.IngredientOrderItemSelector;
import stonering.entity.item.selectors.ItemSelector;
import stonering.enums.items.recipe.Ingredient;
import stonering.enums.materials.MaterialMap;
import stonering.game.GameMvc;
import stonering.game.model.system.items.ItemContainer;
import stonering.util.geometry.Position;
import stonering.util.global.Triple;

import java.util.HashMap;
import java.util.Map;

/**
 * Part of {@link ItemOrder}.
 * Tag and item type taken from recipe. Material and origin are set to any on creation and then can be changed by player. //TODO
 *
 * On creation, all items are observed, combined by material and type, and added to dropdown.
 *
 * //TODO add checkboxes for item types.
 * @author Alexander on 05.01.2019.
 */
public class IngredientOrder {
    public final ItemOrder order;
    public final Ingredient ingredient;
    public final ItemSelector itemSelector;


    public final Map<Triple<String, String, String>, Integer> options; // items, grouped by origin, material, type, to their quantity

    public IngredientOrder(ItemOrder order, Ingredient ingredient) {
        this.order = order;
        this.ingredient = ingredient;
        options = new HashMap<>();
        itemSelector = new IngredientOrderItemSelector(this);
    }

    /**
     * Collects all items with desired tags and types from map, filters unreachable ones,
     * combines them by origin, material, and type, and stores to list.
     */
    void updateOptions(Position position) {
        Triple<String, String, String> cacheTriple = new Triple<>("","", "");
        options.clear();
        for (Item item : GameMvc.instance().getModel().get(ItemContainer.class).util.getItemsForIngredient(ingredient, position)) {
            cacheTriple.set(item.getOrigin(), MaterialMap.instance().getMaterial(item.getMaterial()).getName(), item.getType().title);
            options.put(cacheTriple, options.getOrDefault(cacheTriple, 0));
        }
    }
}
