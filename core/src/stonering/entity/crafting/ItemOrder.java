package stonering.entity.crafting;

import stonering.enums.OrderStatusEnum;
import stonering.enums.items.recipe.Ingredient;
import stonering.enums.items.recipe.Recipe;

import java.util.*;

/**
 * Contains recipe, item name, material id and list of {@link IngredientOrder} for each item part.
 * For each required item part, player can specify item selector, otherwise any appropriate ingredient will be used.
 * Changing order properties during task execution will not update task.
 * Repeated task will update on next iteration.
 * mvp is crafting 1 item from 1 item/material.
 *
 * @author Alexander on 27.10.2018.
 */
public class ItemOrder {
    public final Recipe recipe;
    public final HashMap<String, IngredientOrder> parts; //item parts to their ingredients
    public final List<IngredientOrder> consumed;
    public IngredientOrder main;

    public OrderStatusEnum status;
    public boolean repeated;
    public int amount;

    public ItemOrder(Recipe recipe) {
        this.recipe = recipe;
        amount = 1;
        status = OrderStatusEnum.OPEN;
        parts = new HashMap<>();
        consumed = new ArrayList<>();
        if(recipe.main != null) main = new IngredientOrder(recipe.main);
        for (String itemPart : recipe.parts.keySet()) { // create item partOrder for
            parts.put(itemPart, new IngredientOrder(recipe.parts.get(itemPart)));
        }
        for (Ingredient ingredient : recipe.consumed) {
            consumed.add(new IngredientOrder(ingredient));
        }
    }


    public List<IngredientOrder> getAllIngredients() {
        List<IngredientOrder> ingredientOrders = new ArrayList<>(consumed);
        ingredientOrders.addAll(parts.values());
        if (main != null) ingredientOrders.add(main);
        return ingredientOrders;
    }

    @Override
    public String toString() {
        return "ItemOrder{" + "recipe=" + recipe + '}';
    }
}
