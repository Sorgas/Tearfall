package stonering.entity.crafting;

import stonering.entity.item.Item;
import stonering.entity.item.selectors.IngredientOrderItemSelector;
import stonering.entity.item.selectors.ItemSelector;
import stonering.enums.items.recipe.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Part of {@link ItemOrder}.
 * Tag and item type taken from recipe. Material and origin are set to any on creation and then can be changed by player. //TODO
 *
 * //TODO add checkboxes for item types.
 * @author Alexander on 05.01.2019.
 */
public class IngredientOrder {
    public final Ingredient ingredient;
    public ItemSelector itemSelector;
    public final List<Item> items = new ArrayList<>();

    public IngredientOrder(Ingredient ingredient) {
        this.ingredient = ingredient;

        itemSelector = new IngredientOrderItemSelector(this);
    }

    public IngredientOrder(Ingredient ingredient, ItemSelector selector) {
        this.ingredient = ingredient;
        itemSelector = selector;
    }
}
