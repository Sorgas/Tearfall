package stonering.game.model.system.building;

import stonering.entity.Entity;
import stonering.entity.building.Building;
import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.entity.building.aspects.WorkbenchAspect.OrderTaskEntry;
import stonering.entity.crafting.ItemOrder;
import stonering.entity.job.Task;
import stonering.entity.job.action.CraftItemAction;
import stonering.entity.job.action.TaskTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.tasks.TaskContainer;
import stonering.util.global.Logger;

import java.util.LinkedList;

import static stonering.enums.TaskStatusEnum.*;
import static stonering.enums.TaskStatusEnum.PAUSED;

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
        if(!building.hasAspect(WorkbenchAspect.class)) return;
        WorkbenchAspect aspect = building.getAspect(WorkbenchAspect.class);
        if (aspect.entries.isEmpty() || !aspect.hasActiveOrders) return;
        OrderTaskEntry entry = aspect.entries.getFirst();
        switch (entry.order.status) {
            case OPEN: { // newly added order with no task.
                if (entry.task == null) createTaskForOrder(entry, building);
                break;
            }
            case PAUSED: {
                rollToNextNotSuspended(aspect); // try to move to the next task
                break;
            }
            case COMPLETE:
            case FAILED:
                handleOrderCompletion(aspect, entry); // remove, suspend or move to bottom
        }
    }

    /**
     * Rolls or deletes completed tasks (depending on repeated).
     */
    private void handleOrderCompletion(WorkbenchAspect aspect, OrderTaskEntry entry) {
        if (entry.order.isRepeated()) { // move repeated order to the bottom
            aspect.entries.remove(entry);
            aspect.entries.addLast(entry);
        } else { // remove completed order
            removeOrder(aspect, entry.order);
        }
    }

    /**
     * Suspends or deletes failed order (depending on setting).
     */
    private void handleOrderFail(WorkbenchAspect aspect, OrderTaskEntry entry) {
        if(aspect.deleteFailedTasks) { // delete failed order
            removeOrder(aspect, entry.order);
        } else { // suspend failed order
            entry.task.reset();
            setOrderSuspended(entry.order, true);
        }
    }

    /**
     * Adds order to WB. Orders are always added to the beginning of the list.
     */
    public void addOrder(WorkbenchAspect aspect, ItemOrder order) {
        Logger.TASKS.logDebug("Adding order " + order.toString() + " to " + aspect.getEntity().toString());
        OrderTaskEntry entry = new OrderTaskEntry(order);
        aspect.entries.add(0, entry);
        aspect.updateActiveOrders();
    }

    /**
     * Removes order from workbench. If order was in progress, it is interrupted immediately.
     */
    public void removeOrder(WorkbenchAspect aspect, ItemOrder order) {
        Logger.TASKS.logDebug("Removing order " + order.toString() + " from " + aspect.getEntity().toString());
        OrderTaskEntry entry = findEntry(order);
        if (entry != null) {
            int index = aspect.entries.indexOf(entry);
            aspect.entries.remove(index);
            if (index == 0) failEntryTask(entry); // interrupt currently executing order.
        } else {
            Logger.TASKS.logWarn("Trying to remove unknown order " + order.toString() + " from " + aspect.getEntity().toString());
        }
        aspect.updateActiveOrders();
    }

    private void failEntryTask(OrderTaskEntry entry) {
        if (entry.task != null) entry.task.fail();
    }

    /**
     * Suspends order. If order was in progress, it is interrupted immediately.
     * TODO rework
     */
    public void setOrderSuspended(WorkbenchAspect aspect, ItemOrder order, boolean value) {
        Logger.TASKS.logDebug("Setting order " + order.toString() + " in " + aspect.getEntity().toString() + " suspended: " + value);
        OrderTaskEntry entry = findEntry(order);
        if (entry != null) {
            if (value && entry.task.status == ACTIVE) entry.task.fail(); // interrupt currently executing order.
            entry.order.status = (value ? PAUSED : OPEN);
        }
        aspect.updateActiveOrders();
    }

    /**
     * Sets order as repeated.
     */
    public void setOrderRepeated(ItemOrder order, boolean value) {
        Logger.TASKS.logDebug("Setting order " + order.toString() + " in " + entity.toString() + " repeated: " + value);
        OrderTaskEntry entry = findEntry(order);
        if (entry != null) {
            entry.order.setRepeated(value);
        }
        updateFlag();
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
    private void createTaskForOrder(OrderTaskEntry entry, Entity entity) {
        Logger.BUILDING.logDebug("Creating task for order " + entry.order.recipe.name);
        CraftItemAction action = new CraftItemAction(entry.order, entity);
        entry.task = new Task(entry.order.recipe.name, TaskTypesEnum.CRAFTING, action, 1);
        GameMvc.instance().getModel().get(TaskContainer.class).addTask(entry.task);
    }

    /**
     * Rolls entry list to make first element not suspended.
     * Does nothing if no active orders exist.
     */
    public void rollToNextNotSuspended(WorkbenchAspect aspect) {
        LinkedList<OrderTaskEntry> entries = aspect.entries;
        if (entries.size() < 2 || !aspect.hasActiveOrders) return; // no roll on 1 or 0 entries, or if all orders suspended.
        while (entries.getFirst().order.status == PAUSED) {
            entries.addLast(entries.removeFirst());
        }
    }
}
