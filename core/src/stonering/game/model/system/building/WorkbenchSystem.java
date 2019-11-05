package stonering.game.model.system.building;

import stonering.entity.Entity;
import stonering.entity.building.Building;
import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.entity.building.aspects.WorkbenchAspect.OrderTaskEntry;
import stonering.entity.crafting.ItemOrder;
import stonering.entity.item.Item;
import stonering.entity.job.Task;
import stonering.entity.job.action.CraftItemAction;
import stonering.enums.OrderStatusEnum;
import stonering.enums.TaskStatusEnum;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.task.TaskContainer;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * System for updating orders and tasks in {@link WorkbenchAspect}.
 *
 * @author Alexander on 25.09.2019.
 */
public class WorkbenchSystem {

    /**
     * Checks state of workbench orders.
     * Tries to lead workbench to state when first order is not suspended and has task,
     * i.e. rolls sequence of orders to unsuspended one, creates tasks for orders.
     */
    public void updateWorkbenchState(Building building) {
        WorkbenchAspect aspect = building.getAspect(WorkbenchAspect.class);
        if (aspect == null) return;
        if (aspect.entries.isEmpty() || !aspect.hasActiveOrders) return;
        OrderTaskEntry entry = aspect.entries.getFirst();
        if(entry.order.status != OrderStatusEnum.ACTIVE) dropContainedItems(aspect); // clear workbench from items
        switch (entry.order.status) {
            case OPEN:  // newly added order with no task.
                if (entry.task == null) createTaskForOrder(entry, building);
                break;
            case SUSPENDED:
                rollToNextNotSuspended(aspect); // try to move to the next task
                break;
            case COMPLETE:
                handleOrderCompletion(aspect, entry); // remove, suspend or move to bottom
                break;
            case FAILED:
                handleOrderFail(aspect, entry);
        }
    }

    /**
     * Rolls or deletes completed tasks (depending on repeated).
     * Drops any items from workbench, including product.
     */
    private void handleOrderCompletion(WorkbenchAspect aspect, OrderTaskEntry entry) {
        if (entry.order.repeated) { // move repeated order to the bottom
            aspect.entries.remove(entry);
            aspect.entries.addLast(entry);
        } else { // remove completed order
            removeOrder(aspect, entry.order);
        }
    }

    /**
     * Suspends or deletes failed order (depending on setting).
     * Drops any items from workbench.
     */
    private void handleOrderFail(WorkbenchAspect aspect, OrderTaskEntry entry) {
        if (aspect.deleteFailedTasks) { // delete failed order
            removeOrder(aspect, entry.order);
        } else { // suspend failed order
            entry.task.reset();
            setOrderSuspended(aspect, entry.order, true);
        }
    }

    /**
     * Adds order to WB. Orders are always added to the beginning of the list.
     */
    public void addOrder(WorkbenchAspect aspect, ItemOrder order) {
        Logger.TASKS.logDebug("Adding order " + order.toString() + " to " + aspect.getEntity().toString());
        if(!aspect.recipes.contains(order.recipe)) {
            Logger.TASKS.logError("Order recipe is not allowed by wb.");
            return;
        }
        OrderTaskEntry entry = new OrderTaskEntry(order);
        aspect.entries.add(0, entry);
        aspect.updateActiveOrders();
    }

    /**
     * Removes order from workbench. If order was in progress, it is interrupted immediately.
     */
    public void removeOrder(WorkbenchAspect aspect, ItemOrder order) {
        Logger.TASKS.logDebug("Removing order " + order.toString() + " from " + aspect.getEntity().toString());
        OrderTaskEntry entry = findEntry(aspect, order);
        if (entry != null) {
            int index = aspect.entries.indexOf(entry);
            aspect.entries.remove(index);
            if (index == 0) failEntryTask(entry); // interrupt currently executing order.
        } else {
            Logger.TASKS.logWarn("Trying to remove unknown order " + order.toString() + " from " + aspect.getEntity().toString());
        }
        aspect.updateActiveOrders();
    }

    /**
     * Suspends order. If order was in progress, it is interrupted immediately.
     * TODO rework
     */
    public void setOrderSuspended(WorkbenchAspect aspect, ItemOrder order, boolean value) {
        Logger.TASKS.logDebug("Setting order " + order.toString() + " in " + aspect.getEntity().toString() + " suspended: " + value);
        OrderTaskEntry entry = findEntry(aspect, order);
        if (entry != null) {
            if (value && entry.task != null && entry.task.status == TaskStatusEnum.ACTIVE)
                failEntryTask(entry); // interrupt currently executing order.
            entry.order.status = (value ? OrderStatusEnum.SUSPENDED : OrderStatusEnum.OPEN);
        }
        aspect.updateActiveOrders();
    }

    private void failEntryTask(OrderTaskEntry entry) {
        if (entry.task != null)
            entry.task.status = TaskStatusEnum.FAILED;
    }

    /**
     * Sets order as repeated.
     */
    public void setOrderRepeated(WorkbenchAspect aspect, ItemOrder order, boolean value) {
        Logger.TASKS.logDebug("Setting order " + order.toString() + " in " + aspect.getEntity().toString() + " repeated: " + value);
        OrderTaskEntry entry = findEntry(aspect, order);
        if (entry != null) {
            entry.order.repeated = value;
        }
        aspect.updateActiveOrders();
    }

    private OrderTaskEntry findEntry(WorkbenchAspect aspect, ItemOrder order) {
        return aspect.entries.stream().filter(entry -> entry.order == order).findFirst().orElse(null);
    }

    /**
     * Creates task and adds it to given entry and {@link TaskContainer}.
     */
    private void createTaskForOrder(OrderTaskEntry entry, Entity entity) {
        Logger.BUILDING.logDebug("Creating task for order " + entry.order.recipe.name);
        CraftItemAction action = new CraftItemAction(entry.order, entity);
        entry.task = new Task(entry.order.recipe.name, action, 1);
        GameMvc.instance().getModel().get(TaskContainer.class).addTask(entry.task);
    }

    /**
     * Rolls entry list to make first element not suspended.
     * Does nothing if no active orders exist.
     */
    private void rollToNextNotSuspended(WorkbenchAspect aspect) {
        LinkedList<OrderTaskEntry> entries = aspect.entries;
        if (entries.size() < 2 || !aspect.hasActiveOrders)
            return; // no roll on 1 or 0 entries, or if all orders suspended.

        while (entries.getFirst().order.status == OrderStatusEnum.SUSPENDED) {
            entries.addLast(entries.removeFirst());
        }
    }

    /**
     * Swap entries on positions index and (index + delta). Does nothing, if indexes not in list range.
     */
    public void swapOrders(WorkbenchAspect aspect, ItemOrder order, int delta) {
        int index = aspect.getOrderIndex(order);
        if (aspect.outOfBounds(index)) return;
        int newIndex = index + delta;
        if (aspect.outOfBounds(newIndex)) return;
        WorkbenchAspect.OrderTaskEntry entry = aspect.entries.get(index);
        aspect.entries.set(index, aspect.entries.get(newIndex));
        aspect.entries.set(newIndex, entry);
    }

    /**
     * Removes items from workbench's internal storage, and puts them to random positions on the ground.
     * If workbench is blocked by walls, it cannot drop any items. Not a problem, because units cannot reach it to perform tasks as well.
     */
    private void dropContainedItems(WorkbenchAspect aspect) {
        if(aspect.containedItems.isEmpty()) return;
        List<Position> positions = getPositionsToDrop(aspect);
        if(positions.isEmpty()) return;
        ItemContainer container = GameMvc.instance().getModel().get(ItemContainer.class);
        Random random = new Random();
        for (Item item : aspect.containedItems) {
            container.putItem(item, positions.get(random.nextInt(positions.size())));
        }
        aspect.containedItems.clear();
    }

    private List<Position> getPositionsToDrop(WorkbenchAspect aspect) {
        LocalMap map = GameMvc.instance().getModel().get(LocalMap.class);
        List<Position> positions = map.getAllNeighbourPositions(aspect.getEntity().position, BlockTypesEnum.PassageEnum.PASSABLE);
        if (positions.isEmpty())
            positions = map.getAllNeighbourPositions(aspect.getEntity().position, BlockTypesEnum.PassageEnum.FLY_ONLY);
        return positions;
    }
}
