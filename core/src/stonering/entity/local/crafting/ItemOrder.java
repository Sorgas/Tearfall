package stonering.entity.local.crafting;

import stonering.entity.local.items.Item;
import stonering.entity.local.items.selectors.ItemSelector;
import stonering.entity.local.items.selectors.SimpleItemSelector;
import stonering.enums.items.recipe.ItemPartRecipe;
import stonering.enums.items.recipe.Recipe;
import stonering.game.core.GameMvc;
import stonering.game.core.model.lists.ItemContainer;
import stonering.global.utils.Position;
import stonering.util.global.Pair;

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
    private GameMvc gameMvc;
    private Recipe recipe;
    private List<ItemPartOrder> parts;                            // itemPart to items selected for variant.
    private boolean repeated;
    private boolean suspended;

    private String selectedString;

    public ItemOrder(GameMvc gameMvc, Recipe recipe) {
        this.gameMvc = gameMvc;
        this.recipe = recipe;
        initParts(recipe);
    }

    /**
     * Creates order parts for each recipe part.
     * @param recipe
     */
    private void initParts(Recipe recipe) {
        parts = new ArrayList<>();
        for (ItemPartRecipe itemPartRecipe : recipe.getParts()) {
            parts.add(new ItemPartOrder(gameMvc, this, itemPartRecipe.getName()));
        }
    }

    @Override
    public String toString() {
        return "ItemOrder{" +
                "recipe=" + recipe +
                ", selectedString='" + selectedString + '\'' +
                '}';
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
