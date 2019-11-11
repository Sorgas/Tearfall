package stonering.entity.building.aspects;

import stonering.entity.building.Building;
import stonering.entity.item.Item;
import stonering.entity.job.Task;
import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.entity.crafting.ItemOrder;
import stonering.enums.items.recipe.Recipe;
import stonering.enums.items.recipe.RecipeMap;
import stonering.game.model.system.task.TaskContainer;
import stonering.stage.workbench.WorkbenchMenu;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static stonering.enums.OrderStatusEnum.*;

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
        ((Building) entity).getType().recipes.forEach(s -> recipes.add(RecipeMap.instance().getRecipe(s))); // load all recipes from building type
    }
}
