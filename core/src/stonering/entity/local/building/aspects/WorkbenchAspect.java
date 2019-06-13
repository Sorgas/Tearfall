package stonering.entity.local.building.aspects;

import stonering.entity.job.Task;
import stonering.entity.job.action.CraftItemAction;
import stonering.entity.job.action.TaskTypesEnum;
import stonering.entity.local.Aspect;
import stonering.entity.local.Entity;
import stonering.entity.local.building.Building;
import stonering.entity.local.crafting.ItemOrder;
import stonering.enums.OrderStatusEnum;
import stonering.enums.items.recipe.Recipe;
import stonering.enums.items.recipe.RecipeMap;
import stonering.game.GameMvc;
import stonering.game.model.lists.tasks.TaskContainer;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Aspect for workbenches. Manages orders of workbench.
 * Orders for this workbench are stored in cycled list.
 * After creation order can be cancelled, suspended, moved in the list, set for repeating.
 * Only first order is executed. After executing, order is removed from the list.
 * If order is repeated, instead of removing, it moves to the bottom of the list.
 * If execution is not possible, order is suspended.
 * Suspended entries are skipped.
 * <p>
 * Fail on execution generates general warning for player.
 *
 * @author Alexander on 01.11.2018.
 */
public class WorkbenchAspect extends Aspect {
    public static final String NAME = "workbench";
    private List<Recipe> recipes;
    private LinkedList<OrderTaskEntry> entries; // entry may have no task.

//    private boolean hasActiveOrders = false; // false on empty list or if all orders are suspended

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
        if (entries.isEmpty()) return;
        OrderTaskEntry entry = entries.getFirst();
        if (entry.task == null) {                     // new order
            Logger.BUILDING.logDebug("Initing task for order " + entry.order.getRecipe().name);
            createTaskForOrder(entry);
            GameMvc.instance().getModel().get(TaskContainer.class).getTasks().add(entry.task);
        } else if (entry.task.isFinished()) {          // if task is finished normally
            entries.removeFirst();
            if (entry.order.isRepeated()) {
                entry.task.reset();
                entries.addLast(entry);
            }
            rollToNextNotSuspended();
        }
    }

    /**
     * Rolls entry list to make first element not suspended.
     */
    private void rollToNextNotSuspended() {
        if (entries.size() < 2) return; // no roll on 1 or 0 entries.
        if (entries.stream().allMatch(entry -> entry.order.isPaused())) return; // all orders paused
        while(entries.getFirst().order.isPaused()) {
            entries.addLast(entries.removeFirst());
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
            entry.order.setStatus(value ? OrderStatusEnum.PAUSED : OrderStatusEnum.WAITING);
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
     * Creates task and adds it to given entry.
     */
    private void createTaskForOrder(OrderTaskEntry entry) {
        CraftItemAction action = new CraftItemAction(entry.order, entity);
        entry.task = new Task(entry.order.getRecipe().name, TaskTypesEnum.CRAFTING, action, 1);
    }

    /**
     * Swap entries on positions index and (index + delta). Does nothing, if indexes not in list range.
     */
    public void swapOrders(int index, int delta) {
        if (inBounds(index)) {
            int newIndex = index + delta;
            if (inBounds(newIndex)) {
                OrderTaskEntry entry = entries.get(index);
                entries.set(index, entries.get(newIndex));
                entries.set(newIndex, entry);
            }
        }
    }

    public static class OrderTaskEntry {
        public ItemOrder order;
        public Task task;

        public OrderTaskEntry(ItemOrder order) {
            this.order = order;
        }
    }

    private boolean inBounds(int index) {
        return index >= 0 && index < entries.size();
    }

    private void initRecipes() { //ok
        ((Building) entity).getType().recipes.forEach(s -> recipes.add(RecipeMap.getInstance().getRecipe(s)));
    }

    public List<OrderTaskEntry> getEntries() {
        return entries;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }
}
