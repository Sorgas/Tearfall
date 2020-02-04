package stonering.game.model.system.building;

import stonering.entity.building.Building;
import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.entity.crafting.ItemOrder;
import stonering.entity.item.Item;
import stonering.entity.job.Task;
import stonering.entity.job.action.CraftItemAction;
import stonering.enums.OrderStatusEnum;
import stonering.enums.action.TaskStatusEnum;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.passage.NeighbourPositionStream;
import stonering.game.model.system.EntitySystem;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.task.TaskContainer;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static stonering.enums.OrderStatusEnum.*;

/**
 * System for updating orders and tasks in {@link WorkbenchAspect}, also used to manage orders from ui.
 * <p>
 * Only one order of workbench can be active at a time.
 * When order becomes first in the list, {@link Task} is created and passed to {@link TaskContainer}.
 * Task conditions are checked when task is taken by performer.
 * Order status is updated when task changes status.
 * After creation, order can be cancelled, suspended, moved in the list, set for repeating.
 * After executing, order is removed from the list, or moved to the bottom, if it is repeated.
 * If execution is not possible, order is suspended or cancelled (TODO add config for this).
 * Suspended orders are skipped.
 * Orders are configured via {@link stonering.stage.workbench.WorkbenchMenu}.
 * Fail on execution generates general warning for player.
 *
 * @author Alexander on 25.09.2019.
 */
public class WorkbenchSystem extends EntitySystem<Building> {

    /**
     * Checks state of workbench orders.
     * Tries to lead workbench to state when first order is not suspended and has task,
     * i.e. rolls sequence of orders to unsuspended one, creates tasks for orders.
     */
    @Override
    public void update(Building building) {
        WorkbenchAspect aspect = building.getAspect(WorkbenchAspect.class);
        if (aspect == null || aspect.orders.isEmpty()) return;
        ItemOrder order = aspect.orders.getFirst();
        switch (order.status) {
            case OPEN:
                handleOpenOrder(order, aspect);
                break;
            case ACTIVE:
                handleActiveOrder(order, aspect);
                break;
            case SUSPENDED:
                failAspectTask(aspect);
                rollToNextNotSuspended(aspect); // try to move to the next task
                break;
            case COMPLETE:
                handleOrderCompletion(aspect, order); // remove, suspend or move to bottom
                break;
            case FAILED:
                dropContainedItems(aspect);
                handleOrderFail(aspect, order);
                break;
        }
    }

    private void handleOpenOrder(ItemOrder order, WorkbenchAspect aspect) {
        dropContainedItems(aspect);
        createTaskForOrder(order, aspect);
        order.status = ACTIVE;
    }

    /**
     * Checks task status and update order status accordingly.
     */
    private void handleActiveOrder(ItemOrder order, WorkbenchAspect aspect) {
        switch (aspect.currentTask.status) {
            case COMPLETE:
                aspect.currentTask = null;
                order.status = COMPLETE;
                break;
            case FAILED:
                aspect.currentTask = null;
                order.status = FAILED;
        }
    }

    private void handleOrderCompletion(WorkbenchAspect aspect, ItemOrder order) {
        dropContainedItems(aspect);
        if (order.repeated) { // move repeated order to the bottom
            aspect.orders.remove(order);
            aspect.orders.addLast(order);
            order.status = OPEN;
        } else { // remove completed order
            removeOrder(aspect, order);
        }
    }

    private void handleOrderFail(WorkbenchAspect aspect, ItemOrder order) {
        dropContainedItems(aspect);
        if (aspect.deleteFailedTasks) { // delete failed order
            removeOrder(aspect, order);
        } else { // suspend failed order
            setOrderSuspended(aspect, order, true);
        }
    }

    /**
     * Adds order to WB. Orders are always added to the beginning of the list.
     */
    public void addOrder(WorkbenchAspect aspect, ItemOrder order) {
        Logger.TASKS.logDebug("Adding order " + order.toString() + " to " + aspect.entity.toString());
        if (aspect.recipes.contains(order.recipe)) {
            order.status = OPEN;
            aspect.orders.add(0, order);
        } else {
            Logger.TASKS.logError("Order recipe is not allowed by wb.");
        }
    }

    /**
     * Removes order from workbench. If order was in progress, task is failed.
     */
    public void removeOrder(WorkbenchAspect aspect, ItemOrder order) {
        Logger.TASKS.logDebug("Removing order " + order.toString() + " from " + aspect.entity.toString());
        if (aspect.orders.contains(order)) {
            if (order.status == ACTIVE) failAspectTask(aspect);
            aspect.orders.remove(order);
        } else {
            Logger.TASKS.logWarn("Trying to remove unknown order " + order.toString() + " from " + aspect.entity.toString());
        }
    }

    /**
     * Suspends order. If order was in progress, task is failed.
     */
    public void setOrderSuspended(WorkbenchAspect aspect, ItemOrder order, boolean value) {
        Logger.TASKS.logDebug("Setting order " + order.toString() + " in " + aspect.entity.toString() + " suspended: " + value);
        if (order.status == ACTIVE) failAspectTask(aspect);
        order.status = (value ? OrderStatusEnum.SUSPENDED : OrderStatusEnum.OPEN);
    }

    public void setOrderRepeated(WorkbenchAspect aspect, ItemOrder order, boolean value) {
        Logger.TASKS.logDebug("Setting order " + order.toString() + " in " + aspect.entity.toString() + " repeated: " + value);
        order.repeated = value;
    }

    /**
     * Creates task and adds it to given entry and {@link TaskContainer}.
     */
    private void createTaskForOrder(ItemOrder order, WorkbenchAspect aspect) {
        failAspectTask(aspect);
        Logger.BUILDING.logDebug("Creating task for order " + order.recipe.name);
        CraftItemAction action = new CraftItemAction(order, aspect.entity);
        aspect.currentTask = new Task(order.recipe.name, action, 1);
        GameMvc.instance().model().get(TaskContainer.class).addTask(aspect.currentTask);
    }

    /**
     * Rolls entry list to make first element not suspended.
     * Does nothing if no active orders exist.
     */
    private void rollToNextNotSuspended(WorkbenchAspect aspect) {
        if (aspect.orders.size() < 2) return; // no roll on 1 or 0 orders, or if all orders suspended.
        while (aspect.orders.getFirst().status == OrderStatusEnum.SUSPENDED) {
            aspect.orders.addLast(aspect.orders.removeFirst());
        }
    }

    /**
     * Swap orders on positions index and (index + delta). Does nothing, if indexes not in list range.
     */
    public void swapOrders(WorkbenchAspect aspect, ItemOrder order, int delta) {
        int index = aspect.orders.indexOf(order);
        int newIndex = index + delta;
        if (index < 0 || index >= aspect.orders.size() || newIndex < 0 || newIndex >= aspect.orders.size()) return;
        aspect.orders.set(index, aspect.orders.get(newIndex));
        aspect.orders.set(newIndex, order);
    }

    /**
     * Removes items from workbench's internal storage, and puts them to random positions on the ground.
     * If workbench has no passable tiles around, it cannot drop any items.
     * Not a problem, because units cannot reach it to perform tasks as well.
     */
    private void dropContainedItems(WorkbenchAspect aspect) {
        Logger.BUILDING.logDebug("Dropping contained items from " + aspect.entity);
        if (aspect.containedItems.isEmpty()) return;
        List<Position> positions = getPositionsToDrop(aspect);
        if (positions.isEmpty()) return;
        ItemContainer container = GameMvc.instance().model().get(ItemContainer.class);
        Random random = new Random();
        for (Item item : new ArrayList<>(aspect.containedItems)) {
            container.containedItemsSystem.removeItemFromWorkbench(item, aspect);
            container.onMapItemsSystem.putItem(item, positions.get(random.nextInt(positions.size())));
        }
        aspect.containedItems.clear();
    }

    private List<Position> getPositionsToDrop(WorkbenchAspect aspect) {
        return new NeighbourPositionStream(aspect.entity.position)
                .filterSameZLevel()
                .filterByPassage(BlockTypeEnum.PassageEnum.PASSABLE)
                .stream.collect(Collectors.toList());
    }

    private void failAspectTask(WorkbenchAspect aspect) {
        if (aspect.currentTask != null) aspect.currentTask.status = TaskStatusEnum.FAILED;
        aspect.currentTask = null;
    }
}
