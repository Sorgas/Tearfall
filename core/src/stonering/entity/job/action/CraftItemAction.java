package stonering.entity.job.action;

import stonering.entity.building.aspects.FuelConsumerAspect;
import stonering.entity.crafting.IngredientOrder;
import stonering.entity.job.action.target.EntityActionTarget;
import stonering.entity.Entity;
import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.entity.crafting.ItemOrder;
import stonering.entity.item.Item;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.item.ItemsStream;
import stonering.generators.items.ItemGenerator;
import stonering.util.global.Logger;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Action for crafting item by item order on workbench. Items for crafting will be brought to WB.
 * WB should have {@link WorkbenchAspect} and {@link ItemContainerAspect}.
 *
 * @author Alexander on 06.01.2019.
 */
public class CraftItemAction extends Action {
    private ItemOrder itemOrder;
    private Entity workbench;

    public CraftItemAction(ItemOrder itemOrder, Entity workbench) {
        super(new EntityActionTarget(workbench, ActionTargetTypeEnum.NEAR)); // unit will stand near wb while performing task
        this.itemOrder = itemOrder;
        this.workbench = workbench;
    }

    /**
     * Creates item, consumes ingredients. Product item is put to Workbench.
     */
    @Override
    protected void performLogic() {
        ItemContainer container = GameMvc.instance().model().get(ItemContainer.class);
        WorkbenchAspect workbenchAspect = workbench.getAspect(WorkbenchAspect.class);
        Item product = new ItemGenerator().generateItemByOrder(itemOrder);
        // spend components
        List<Item> items = itemOrder.getAllIngredients().stream().map(IngredientOrder::getItem).collect(Collectors.toList());
        container.containedItemsSystem.removeItemsFromWorkbench(items, workbenchAspect);
        container.removeItems(items);
        // put product into wb
        container.addItem(product);
        container.containedItemsSystem.addItemToWorkbench(product, workbenchAspect);
    }

    /**
     * Checks that action conditions are met. Creates sub action otherwise.
     * TODO check ingredients and fuel availability before bringing something to workbench.
     * TODO add usage of items in nearby containers.
     */
    @Override
    public int check() {
        WorkbenchAspect aspect = workbench.getAspect(WorkbenchAspect.class);
        if (workbench.getAspect(WorkbenchAspect.class) == null)
            return Logger.TASKS.logWarn("Building " + workbench.toString() + " is not a workbench.", FAIL);
        int orderCheckResult = checkIngredientItems(aspect);
        if (orderCheckResult != OK) return orderCheckResult;
        if (workbench.hasAspect(FuelConsumerAspect.class) && !workbench.getAspect(FuelConsumerAspect.class).isFueled()) { // workbench requires fuel
            task.addFirstPreAction(new FuelingAciton(workbench));
            return NEW;
        }
        return OK;
    }

    /**
     * Checks that all ingredients have selected items.
     * Creates actions for bringing items to WB. Clears ingredients with 'spoiled' items.
     * Returns fail, if item for some ingredient cannot be found.
     */
    private int checkIngredientItems(WorkbenchAspect aspect) {
        for (IngredientOrder order : itemOrder.getAllIngredients()) {
            if (checkIngredient(order, aspect)) continue;
            if (order.item != null) System.out.println("'spoiled' item in ingredient order"); // free item TODO locking
            if ((order.item = selectItemForIngredientFromMap(order)) == null) return FAIL; // no valid item found
            if (aspect.containedItems.contains(order.item)) continue; // item in WB, no actions required
            task.addFirstPreAction(new PutItemAction(order.item, workbench)); // create action to bring item
            return NEW; // new action is created
        }
        return OK; // all ingredients have valid items
    }

    /**
     * Checks if ingredient item is valid (matches ingredient and stored in WB).
     */
    private boolean checkIngredient(IngredientOrder order, WorkbenchAspect aspect) {
        return order.item != null && order.itemSelector.checkItem(order.item) && aspect.containedItems.contains(order.item);
    }

    private Item selectItemForIngredientFromMap(IngredientOrder ingredientOrder) {
        ItemContainer container = GameMvc.instance().model().get(ItemContainer.class);
        List<Item> foundItems = new ItemsStream()
                .filterOnMap()
                .filterBySelector(ingredientOrder.itemSelector)
                .filterByReachability(task.performer.position)
                .toList(); // items from map that match the ingredient
        return foundItems.isEmpty() ? null : container.util.getNearestItems(foundItems, task.performer.position, 1).get(0);
    }

    @Override
    public String toString() {
        return "Crafting action: " + itemOrder.toString();
    }
}
