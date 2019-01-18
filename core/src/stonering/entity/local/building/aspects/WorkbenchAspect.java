package stonering.entity.local.building.aspects;

import stonering.entity.jobs.Task;
import stonering.entity.jobs.actions.CraftItemAction;
import stonering.entity.jobs.actions.TaskTypesEnum;
import stonering.entity.local.Aspect;
import stonering.entity.local.AspectHolder;
import stonering.entity.local.building.Building;
import stonering.entity.local.crafting.ItemOrder;
import stonering.entity.local.items.Item;
import stonering.enums.items.recipe.Recipe;
import stonering.enums.items.recipe.RecipeMap;
import stonering.util.global.TagLoggersEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Aspect for workbenches. Manages entries of workbench.
 * Orders for this workbench are stored in cycled list.
 * After creation order can be cancelled, suspended, moved in the list, set for repeating.
 * Only first order can be executed. After executing, order id removed from the list.
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
    private List<Item> storage;  //TODO move to separate aspect
    private List<OrderTaskEntry> entries; // entry may have no task.

    private int current = -1;
    private boolean hasActiveOrders = false; // false on empty list or if all orders are suspended

    public WorkbenchAspect(AspectHolder aspectHolder) {
        super(aspectHolder);
        entries = new ArrayList<>();
        recipes = new ArrayList<>();
        storage = new ArrayList<>();
        initRecipes();
    }

    /**
     * Checks task of current order and moves to next not suspended order if it's finished.
     */
    @Override
    public void turn() {
        if (entries.isEmpty() || !hasActiveOrders) return;
        OrderTaskEntry entry = entries.get(current);
        if (entry.task == null) {                     // new order
            TagLoggersEnum.BUILDING.logDebug("Initing task for order " + entry.order.getRecipe().getName());
            createTaskForOrder(entry);
            gameContainer.getTaskContainer().getTasks().add(entry.task);
        } else if (entry.task.isFinished()) {          // if task is finished normally
            if (entry.order.isRepeated()) {
                entry.task.reset();
            } else {
                entries.remove(current);
                current--;
            }
            rollToNextNotSuspended();
        }
    }

    /**
     * Moves current pointer to next not suspended order.
     */
    private void rollToNextNotSuspended() {
        if (entries.size() < 2) return; // no roll on 1 or 0 entries.
        int previous = current;
        do {
            current++;
            if (current >= entries.size()) current = 0;
        } while (entries.get(current).order.isSuspended() || current != previous);
    }

    /**
     * Adds order to WB. Orders are always added to the beginning of the list.
     */
    public void addOrder(ItemOrder order) {
        TagLoggersEnum.TASKS.logDebug("Adding order " + order.toString() + " to " + this.getName());
        OrderTaskEntry entry = new OrderTaskEntry(order);
        entries.add(0, entry);
        updateFlag();
        current++;
    }

    /**
     * Removes order from workbench. If order was in progress, it is interrupted immediately.
     */
    public void removeOrder(ItemOrder order) {
        TagLoggersEnum.TASKS.logDebug("Removing order " + order.toString() + " from " + this.getName());
        OrderTaskEntry entry = findEntry(order);
        if (entry != null) {
            int index = entries.indexOf(entry);
            entries.remove(index);
            if (index == current) failEntryTask(entry); // interrupt currently executing order.
            if (index <= current) {
                current--;
            }
        }
        updateFlag();
    }

    private void failEntryTask(OrderTaskEntry entry) {
        if (entry.task != null) entry.task.fail();
    }

    /**
     * Suspends order. If order was in progress, it is interrupted immediately.
     */
    public void setOrderSuspended(ItemOrder order, boolean value) {
        TagLoggersEnum.TASKS.logDebug("Setting order " + order.toString() + " in " + this.getName() + "suspended: " + value);
        OrderTaskEntry entry = findEntry(order);
        if (entry != null) {
            if (value) {
                int index = entries.indexOf(entry);
                if (index == current) entry.task.fail(); // interrupt currently executing order.
            }
            entry.order.setSuspended(value);
        }
        updateFlag();
    }

    /**
     * Sets order as repeated.
     */
    public void setOrderRepeated(ItemOrder order, boolean value) {
        TagLoggersEnum.TASKS.logDebug("Setting order " + order.toString() + " in " + this.getName() + "repeated: " + value);
        OrderTaskEntry entry = findEntry(order);
        if (entry != null) {
            entry.order.setRepeated(value);
        }
        updateFlag();
    }

    private void updateFlag() {
        hasActiveOrders = false;
        for (OrderTaskEntry entry : entries) {
            if (!entry.order.isSuspended()) {
                hasActiveOrders = true;
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
        CraftItemAction action = new CraftItemAction(entry.order, aspectHolder);
        entry.task = new Task(entry.order.getRecipe().getName(), TaskTypesEnum.CRAFTING, action, 1);
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

        ItemOrder order;
        Task task;

        public OrderTaskEntry(ItemOrder order) {
            this.order = order;
        }

        public ItemOrder getOrder() {
            return order;
        }

    }

    private boolean inBounds(int index) {
        return index >= 0 && index < entries.size();
    }

    private void initRecipes() { //ok
        ((Building) aspectHolder).getType().getRecipes().forEach(s -> recipes.add(RecipeMap.getInstance().getRecipe(s)));
    }

    public List<OrderTaskEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<OrderTaskEntry> entries) {
        this.entries = entries;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public List<Item> getStorage() {
        return storage;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
