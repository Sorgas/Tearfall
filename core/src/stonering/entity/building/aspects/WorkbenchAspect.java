package stonering.entity.building.aspects;

import stonering.entity.building.Building;
import stonering.entity.item.Item;
import stonering.entity.job.Task;
import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.entity.crafting.ItemOrder;
import stonering.enums.items.recipe.Recipe;
import stonering.enums.items.recipe.RecipeMap;
import stonering.util.logging.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.math.MathUtils;

/**
 * Aspect for workbenches. Stores {@link ItemOrder}s in a list and {@link Task} of a currently active order.
 *
 * @author Alexander on 01.11.2018.
 */
public class WorkbenchAspect extends Aspect {
    public final List<Recipe> recipes; // all available recipes
    public final LinkedList<ItemOrder> orders; // entry may have no task.
    public final List<Item> containedItems; // items for order can be brought here, and will be dropped if order changes.

    public Task currentTask;

    public boolean deleteFailedTasks = false; // setting for deleting or suspending failed tasks.

    public WorkbenchAspect(Entity entity) {
        super(entity);
        recipes = new ArrayList<>();
        orders = new LinkedList<>();
        containedItems = new ArrayList<>();
        ((Building) entity).type.recipes.forEach(s -> recipes.add(RecipeMap.instance().getRecipe(s))); // load all recipes from building type
    }

    public boolean moveOrder(int index, int delta) {
        if (index < 0 || index >= orders.size())
            return Logger.CRAFTING.logWarn("Attempt to move order with invalid index " + index, false);
        int newIndex = MathUtils.clamp(index + delta, 0, orders.size());
        if (newIndex == index) return false;
        ItemOrder order = orders.remove(index);
        orders.add(newIndex, order);
        return true;
    }
}
