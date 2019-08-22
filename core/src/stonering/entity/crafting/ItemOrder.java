package stonering.entity.crafting;

import stonering.enums.TaskStatusEnum;
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
    public final HashMap<String, IngredientOrder> parts;
    public final List<IngredientOrder> consumed;
    private TaskStatusEnum status;
    private boolean repeated;
    private int amount;

    public ItemOrder(Recipe recipe) {
        this.recipe = recipe;
        amount = 1;
        status = TaskStatusEnum.OPEN;
        parts = new HashMap<>();
        consumed = new ArrayList<>();
        for (String itemPart : recipe.parts.keySet()) { // create item partOrder for
            parts.put(itemPart, new IngredientOrder(this, recipe.parts.get(itemPart)));
        }
        for (Ingredient ingredient : recipe.consumed) {
            consumed.add(new IngredientOrder(this, ingredient));
        }
    }

    public boolean isPaused() {
        return status == TaskStatusEnum.PAUSED;
    }

    @Override
    public String toString() {
        return "ItemOrder{" + "recipe=" + recipe + '}';
    }

    public TaskStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TaskStatusEnum status) {
        this.status = status;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isRepeated() {
        return repeated;
    }

    public void setRepeated(boolean repeated) {
        this.repeated = repeated;
    }
}
