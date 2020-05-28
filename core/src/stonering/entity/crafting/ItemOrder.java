package stonering.entity.crafting;

import stonering.enums.OrderStatusEnum;
import stonering.enums.items.recipe.Ingredient;
import stonering.enums.items.recipe.Recipe;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Contains recipe, item name, material id and list of {@link IngredientOrder} for each item part.
 * For each required item part, player can specify item selector, otherwise any appropriate ingredient will be used.
 * Changing order properties during task execution will not update task.
 * Repeated task will update on next iteration.
 * mvp is crafting 1 item from 1 item/material.
 *
 * @author Alexander on 27.10.2018.
 */
public class ItemOrder extends ItemConsumingOrder {
    public final Recipe recipe;

    public OrderStatusEnum status;
    public boolean repeated;
    public int amount;

    public ItemOrder(Recipe recipe) {
        super();
        this.recipe = recipe;
        amount = 1;
        status = OrderStatusEnum.OPEN;
        recipe.ingredients.forEach((key, ingredient) ->
                ingredientOrders.put(key, new IngredientOrder(ingredient)));
    }

    @Override
    public String toString() {
        return "ItemOrder{" + "recipe=" + recipe + '}';
    }
}
