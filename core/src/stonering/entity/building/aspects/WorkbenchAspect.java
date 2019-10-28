package stonering.entity.building.aspects;

import stonering.entity.building.Building;
import stonering.entity.job.ItemOrderTask;
import stonering.entity.job.Task;
import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.entity.crafting.ItemOrder;
import stonering.enums.items.recipe.Recipe;
import stonering.enums.items.recipe.RecipeMap;
import stonering.game.model.system.tasks.TaskContainer;
import stonering.stage.workbench.WorkbenchMenu;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static stonering.enums.TaskStatusEnum.*;

/**
 * Aspect for workbenches. Manages (crafting) orders of workbench.
 * Orders for workbench are stored in cycled list. When order becomes first in the list, {@link Task} is created and passed to {@link TaskContainer}.
 * Task conditions are checked when task is taken by performer.
 * Order status id updated when task changes status.
 * <p>
 * After creation, order can be cancelled, suspended, moved in the list, set for repeating.
 * After executing, order is removed from the list, or moved to the bottom, if it is repeated.
 * If execution is not possible, order is suspended or cancelled (TODO add config for this).
 * Suspended entries are skipped.
 * Orders are configured via {@link WorkbenchMenu}.
 * <p>
 * Fail on execution generates general warning for player.
 *
 * @author Alexander on 01.11.2018.
 */
public class WorkbenchAspect extends Aspect {
    public final List<Recipe> recipes; // all available recipes
    public final LinkedList<OrderTaskEntry> entries; // entry may have no task.
    public boolean hasActiveOrders = false; // false on empty list or if all orders are suspended

    public boolean deleteFailedTasks = false; // setting for deleting or suspending failed tasks.

    public WorkbenchAspect(Entity entity) {
        super(entity);
        entries = new LinkedList<>();
        recipes = new ArrayList<>();
        ((Building) entity).getType().recipes.forEach(s -> recipes.add(RecipeMap.instance().getRecipe(s)));
    }

    public static class OrderTaskEntry {
        public ItemOrder order;
        public ItemOrderTask task;

        public OrderTaskEntry(ItemOrder order) {
            this.order = order;
        }
    }

    public int getOrderIndex(ItemOrder order) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).order.equals(order)) return i;
        }
        Logger.TASKS.logError("Getting index of item order " + order + " that is not in workbench " + toString());
        return -1;
    }

    /**
     * Recounts active orders.
     */
    public void updateActiveOrders() {
        hasActiveOrders = entries.stream().anyMatch(entry -> entry.order.status == OPEN || entry.order.status == ACTIVE);
    }

    public boolean outOfBounds(int index) {
        return index < 0 || index >= entries.size();
    }

    public List<OrderTaskEntry> getEntries() {
        return entries;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }
}
