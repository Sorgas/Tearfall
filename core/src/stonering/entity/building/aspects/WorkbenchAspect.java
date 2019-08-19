package stonering.entity.building.aspects;

import stonering.entity.building.Building;
import stonering.entity.job.Task;
import stonering.entity.job.action.CraftItemAction;
import stonering.entity.job.action.TaskTypesEnum;
import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.entity.crafting.ItemOrder;
import stonering.enums.items.recipe.Recipe;
import stonering.enums.items.recipe.RecipeMap;
import stonering.game.GameMvc;
import stonering.game.model.lists.tasks.TaskContainer;
import stonering.game.view.render.stages.workbench.oldmenu.WorkbenchMenuq;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static stonering.enums.TaskStatusEnum.*;

/**
 * Aspect for workbenches. Manages orders of workbench.
 * Orders for workbench are stored in cycled list.
 * Orders are configured via {@link WorkbenchMenuq}.
 * When order becomes first in the list, {@link Task} is created.
 * Order status id updated when task changes status.
 * After creation, order can be cancelled, suspended, moved in the list, set for repeating.
 * Only first order is executed. After executing, order is removed from the list, or moved to the bottom, if it is repeated.
 * If execution is not possible, order is suspended or cancelled (TODO add config for this).
 * Suspended entries are skipped.
 * <p>
 * Fail on execution generates general warning for player.
 *
 * @author Alexander on 01.11.2018.
 */
public class WorkbenchAspect extends Aspect {
    public static final String NAME = "workbench";
    private List<Recipe> recipes; // all available recipes
    private LinkedList<OrderTaskEntry> entries; // entry may have no task.
    private boolean hasActiveOrders = false; // false on empty list or if all orders are suspended
    private boolean deleteFailedTasks = false; // setting for deleting or suspending failed tasks.

    public WorkbenchAspect(Entity entity) {
        super(entity);
        entries = new LinkedList<>();
        recipes = new ArrayList<>();
        initRecipes();
    }

    /**
     * Checks task of order and moves to next not suspended order if it's finished.
     */
    @Override
    public void turn() {
        if (entries.isEmpty() || !hasActiveOrders) return;
        OrderTaskEntry entry = entries.getFirst();
        switch (entry.order.getStatus()) {
            case OPEN: {
                // newly added order with no task.
                if (entry.task == null) createTaskForOrder(entry);
                break;
            }
            case PAUSED: {
                rollToNextNotSuspended(); // try to move to the next task
                break;
            }
            case COMPLETE:
            case FAILED:
                handleOrderCompletion(entry); // remove, suspend or move to bottom
        }
    }

    /**
     * Rolls entry list to make first element not suspended.
     */
    private void rollToNextNotSuspended() {
        if (entries.size() < 2 || !hasActiveOrders) return; // no roll on 1 or 0 entries, or if all orders suspended.
        while (entries.getFirst().order.isPaused()) {
            entries.addLast(entries.removeFirst());
        }
    }

    /**
     * Pauses or deletes failed task (depending on setting).
     * Rolls or deletes completed tasks (depending on repeated).
     */
    private void handleOrderCompletion(OrderTaskEntry entry) {
        entries.remove(entry);
        entry.task.reset();
        if (entry.order.getStatus() == COMPLETE && entry.order.isRepeated()) {
            entries.addLast(entry); // move to the bottom of the list
        } else if (entry.order.getStatus() == FAILED && !deleteFailedTasks) {
            entry.order.setStatus(PAUSED); // pause failed task
        }
    }

    /**
     * Adds order to WB. Orders are always added to the beginning of the list.
     */
    public void addOrder(ItemOrder order) {
        Logger.TASKS.logDebug("Adding order " + order.toString() + " to " + NAME);
        OrderTaskEntry entry = new OrderTaskEntry(order);
        entries.add(0, entry);
        updateFlag();
    }

    /**
     * Removes order from workbench. If order was in progress, it is interrupted immediately.
     */
    public void removeOrder(ItemOrder order) {
        Logger.TASKS.logDebug("Removing order " + order.toString() + " from " + NAME);
        OrderTaskEntry entry = findEntry(order);
        if (entry != null) {
            int index = entries.indexOf(entry);
            entries.remove(index);
            if (index == 0) failEntryTask(entry); // interrupt currently executing order.
        } else {
            Logger.TASKS.logWarn("Trying to remove unknown order " + order.toString() + " from " + NAME);
        }
        updateFlag();
    }

    private void failEntryTask(OrderTaskEntry entry) {
        if (entry.task != null) entry.task.fail();
    }

    /**
     * Suspends order. If order was in progress, it is interrupted immediately.
     * TODO rework
     */
    public void setOrderSuspended(ItemOrder order, boolean value) {
        Logger.TASKS.logDebug("Setting order " + order.toString() + " in " + NAME + "suspended: " + value);
        OrderTaskEntry entry = findEntry(order);
        if (entry != null) {
            if (value) {
                int index = entries.indexOf(entry);
//                if (index == current) entry.task.fail(); // interrupt currently executing order.
            }
            entry.order.setStatus(value ? PAUSED : OPEN);
        }
        updateFlag();
    }

    /**
     * Sets order as repeated.
     */
    public void setOrderRepeated(ItemOrder order, boolean value) {
        Logger.TASKS.logDebug("Setting order " + order.toString() + " in " + NAME + "repeated: " + value);
        OrderTaskEntry entry = findEntry(order);
        if (entry != null) {
            entry.order.setRepeated(value);
        }
        updateFlag();
    }

    private void updateFlag() {
//        hasActiveOrders = false;
        for (OrderTaskEntry entry : entries) {
            if (entry.order.isPaused()) {
//                hasActiveOrders = true;
                break;
            }
        }
    }

    private OrderTaskEntry findEntry(ItemOrder order) {
        OrderTaskEntry found = null;
        for (OrderTaskEntry entry : entries) {
            if (entry.order == order) {
                found = entry;
            }
        }
        return found;
    }

    /**
     * Creates task and adds it to given entry and {@link TaskContainer}.
     */
    private void createTaskForOrder(OrderTaskEntry entry) {
        Logger.BUILDING.logDebug("Creating task for order " + entry.order.getRecipe().name);
        CraftItemAction action = new CraftItemAction(entry.order, entity);
        entry.task = new Task(entry.order.getRecipe().name, TaskTypesEnum.CRAFTING, action, 1);
        GameMvc.instance().getModel().get(TaskContainer.class).addTask(entry.task);
    }

    /**
     * Swap entries on positions index and (index + delta). Does nothing, if indexes not in list range.
     */
    public void swapOrders(ItemOrder order, int delta) {
        int index = getOrderIndex(order);
        if (!inBounds(index)) return;
        int newIndex = index + delta;
        if (!inBounds(newIndex)) return;
        OrderTaskEntry entry = entries.get(index);
        entries.set(index, entries.get(newIndex));
        entries.set(newIndex, entry);
    }

    public static class OrderTaskEntry {
        public ItemOrder order;
        public Task task;

        public OrderTaskEntry(ItemOrder order) {
            this.order = order;
        }
    }

    private int getOrderIndex(ItemOrder order) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).order.equals(order)) return i;
        }
        Logger.TASKS.logError("Getting index of item order " + order + " that is not in workbench " + toString());
        return -1;
    }

    private boolean inBounds(int index) {
        return index >= 0 && index < entries.size();
    }

    private void initRecipes() { //ok
        ((Building) entity).getType().recipes.forEach(s -> recipes.add(RecipeMap.instance().getRecipe(s)));
    }

    public List<OrderTaskEntry> getEntries() {
        return entries;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }
}
