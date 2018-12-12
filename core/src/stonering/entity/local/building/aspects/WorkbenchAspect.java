package stonering.entity.local.building.aspects;

import stonering.entity.jobs.Task;
import stonering.entity.jobs.actions.Action;
import stonering.entity.jobs.actions.TaskTypesEnum;
import stonering.entity.local.Aspect;
import stonering.entity.local.AspectHolder;
import stonering.entity.local.building.Building;
import stonering.entity.local.crafting.ItemOrder;
import stonering.enums.items.Recipe;
import stonering.enums.items.RecipeMap;
import stonering.utils.global.TagLoggersEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Aspect for workbenches. Manages orders of workbench.
 * Orders for this workbench are stored in cycled list.
 * After creation order can be cancelled, suspended, moved in the list, set for repeating.
 * Only first order can be executed. After executing, order id removed from the list.
 * If order is repeated, instead of removing, it moves to the bottom of the list.
 * If execution is not possible, order is suspended.
 * Suspended orders are skipped.
 * <p>
 * Fail on execution generates general warning for player.
 *
 * @author Alexander on 01.11.2018.
 */
public class WorkbenchAspect extends Aspect {
    public static String NAME = "workbench";
    private List<Recipe> recipes; // list of possible recipes

    // list of orders.
    private List<ItemOrder> orders;

    public WorkbenchAspect(AspectHolder aspectHolder) {
        super(NAME, aspectHolder);
        orders = new ArrayList<>();
        recipes = new ArrayList<>();
        initRecipes();
    }

    /**
     * Swap orders on positions index and (index + delta).
     */
    public void swapOrders(int index, int delta) {
        if (inBounds(index)) {
            int newIndex = index + delta;
            if (inBounds(newIndex)) {
                ItemOrder order = orders.get(index);
                orders.set(index, orders.get(newIndex));
                orders.set(newIndex, order);
            }
        }
    }

    /**
     * Called on order finish. Removes it from list, or moves to bottom, if it was repeated.
     */
    private void finishOrder(ItemOrder order) {
        if (orders.contains(order)) {
            if (!order.isRepeated()) { // remove not repeated order
                orders.remove(order);
            } else {
                if (orders.get(0) == order) { // move to bottom if was first. (position can be changed during execution).
                    moveToBottom(order);
                }
            }
            rollToNextNotSuspended();
        } else {
            TagLoggersEnum.TASKS.logDebug("Finishing order " + order + " on wrong workbench " + toString());
            new Throwable().printStackTrace();
        }
    }

    /**
     * Moves all suspended orders in the beginning of the list, to the bottom of it.
     * Does nothing, if there is no active orders in list (player should add active order).
     */
    private void rollToNextNotSuspended() {
        int nextOrderIndex = findUnsuspendedOrders();
        if (nextOrderIndex >= 0) {
            for (int i = 0; i < nextOrderIndex; i++) {
                moveToBottom(orders.get(0));
            }
        }
    }

    /**
     * Moves given order to the bottom of the list.
     */
    private void moveToBottom(ItemOrder order) {
        orders.remove(order);
        orders.add(order);
    }

    /**
     * Generates Task for first order
     */
    public void generateTaskForNextOrder() {
        ItemOrder order = orders.get(0);
        Action action = new Action(gameContainer);
        //TODO add action aspects
        Task task = new Task(order.getRecipe().getName(), TaskTypesEnum.CRAFTING, action, 1, gameContainer);
    }

    /**
     * @return index of first order which is not suspended.
     */
    private int findUnsuspendedOrders() {
        for (int i = 0; i < orders.size(); i++) {
            if (!orders.get(i).isSuspended()) {
                return i;
            }
        }
        return -1;
    }

    private void perform() {

    }

    private boolean inBounds(int index) {
        return index >= 0 && index < orders.size();
    }

    private void initRecipes() { //ok
        ((Building) aspectHolder).getType().getRecipes().forEach(s -> recipes.add(RecipeMap.getInstance().getRecipe(s)));
    }

    public List<ItemOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<ItemOrder> orders) {
        this.orders = orders;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
}
