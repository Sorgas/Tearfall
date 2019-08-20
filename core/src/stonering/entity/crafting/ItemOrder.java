package stonering.entity.crafting;

import stonering.enums.TaskStatusEnum;
import stonering.enums.items.recipe.ItemPartRecipe;
import stonering.enums.items.recipe.Recipe;

import java.util.*;

/**
 * Contains recipe, item name, material id and list of {@link ItemPartOrder} for each item part.
 * For each required item part, player can specify item selector, otherwise any appropriate ingredient will be used.
 * Changing order properties during task execution will not update task.
 * Repeated task will update on next iteration.
 * mvp is crafting 1 item from 1 item/material.
 *
 * @author Alexander on 27.10.2018.
 */
public class ItemOrder {
    public final Recipe recipe;
    private List<ItemPartOrder> parts; // itemPart to item selected for variant.
    private int amount;
    private TaskStatusEnum status;
    private boolean repeated;

    public ItemOrder(Recipe recipe) {
        this.recipe = recipe;
        amount = 1;
        status = TaskStatusEnum.OPEN;
        parts = new ArrayList<>();
        for (ItemPartRecipe itemPartRecipe : recipe.parts) {
            parts.add(new ItemPartOrder(this, itemPartRecipe));
        }
    }

    public boolean isPaused() {
        return status == TaskStatusEnum.PAUSED;
    }

    @Override
    public String toString() {
        return "ItemOrder{" + "recipe=" + recipe + '}';
    }

    public List<ItemPartOrder> getParts() {
        return parts;
    }

    public void setParts(List<ItemPartOrder> parts) {
        this.parts = parts;
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
