package stonering.entity.local.crafting;

import stonering.enums.OrderStatusEnum;
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
    private List<ItemPartOrder> parts; // itemPart to items selected for variant.
    private OrderStatusEnum status;

    private int amount;
    private boolean repeated;

    public ItemOrder() {
        amount = 1;
        status = OrderStatusEnum.WAITING;
        parts = new ArrayList<>();
    }

    public ItemOrder(Recipe recipe) {
        this();
        if(recipe == null) return;
        this.recipe = recipe;
        for (ItemPartRecipe itemPartRecipe : recipe.getParts()) {
            parts.add(new ItemPartOrder(this, itemPartRecipe.getName()));
        }
    }

    /**
     * Order is defined, if all itemPartOrders have itemSelectors.
     */
    public boolean isDefined() {
        for (ItemPartOrder part : parts) {
            if (part.getSelected() == null) return false;
        }
        return true;
    }

    public boolean isPaused() {
        return status == OrderStatusEnum.PAUSED || status == OrderStatusEnum.SUSPENDED;
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

    public OrderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(OrderStatusEnum status) {
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
