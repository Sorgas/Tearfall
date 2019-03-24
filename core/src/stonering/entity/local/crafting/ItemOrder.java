package stonering.entity.local.crafting;

import stonering.enums.items.recipe.ItemPartRecipe;
import stonering.enums.items.recipe.Recipe;
import stonering.game.GameMvc;

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
    private List<ItemPartOrder> parts;                            // itemPart to items selected for variant.
    private boolean repeated;
    private boolean suspended;

    private String selectedString;

    public ItemOrder(Recipe recipe) {
        this.recipe = recipe;
        initParts(recipe);
    }

    /**
     * Creates order parts for each recipe part.
     *
     * @param recipe
     */
    private void initParts(Recipe recipe) {
        parts = new ArrayList<>();
        for (ItemPartRecipe itemPartRecipe : recipe.getParts()) {
            parts.add(new ItemPartOrder(this, itemPartRecipe.getName()));
        }
    }

    @Override
    public String toString() {
        return "ItemOrder{" +
                "recipe=" + recipe +
                ", selectedString='" + selectedString + '\'' +
                '}';
    }

    /**
     * Order is finished, if all itemPartOrders have itemSelectors.
     */
    public boolean isDefined() {
        for (ItemPartOrder part : parts) {
            if (part.getSelected() == null) return false;
        }
        return true;
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

    public String getSelectedString() {
        return selectedString;
    }

    public void setSelectedString(String selectedString) {
        this.selectedString = selectedString;
    }

    public boolean isRepeated() {
        return repeated;
    }

    public void setRepeated(boolean repeated) {
        this.repeated = repeated;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }
}
