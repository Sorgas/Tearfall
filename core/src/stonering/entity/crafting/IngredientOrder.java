package stonering.entity.crafting;

import stonering.entity.item.Item;
import stonering.entity.item.selectors.IngredientOrderItemSelector;
import stonering.entity.item.selectors.ItemSelector;
import stonering.enums.items.recipe.Ingredient;

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
    public final Ingredient ingredient;
    public ItemSelector itemSelector;
    public Item item;

    public IngredientOrder(Ingredient ingredient) {
        this.ingredient = ingredient;
        itemSelector = new IngredientOrderItemSelector(this);
    }

    public Item getItem() {
        return item;
    }
}
