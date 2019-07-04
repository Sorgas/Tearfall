package stonering.entity.crafting;

import stonering.enums.TaskStatusEnum;
import stonering.enums.items.recipe.ItemPartRecipe;
import stonering.enums.items.recipe.Recipe;

import java.util.*;

/**
 * Contains recipe, item NAME and material id for crafting item.
 * Task for crafting creation is based on this.
 * <p>
 * For each required item part, player should specify item selector.
 * <p>
 * Changing order properties during task execution will not update task.
 * Repeated task will update on next iteration.
 * <p>
 * mvp is crafting 1 item from 1 item/material.
 *
 * @author Alexander on 27.10.2018.
 */
public class ItemOrder {
    private Recipe recipe;
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
            parts.add(new ItemPartOrder(this, itemPartRecipe.itemPart));
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

    public Recipe getRecipe() {
        return recipe;
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
