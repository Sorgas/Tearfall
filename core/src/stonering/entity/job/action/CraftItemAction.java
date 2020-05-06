package stonering.entity.job.action;

import stonering.entity.building.aspects.FuelConsumerAspect;
import stonering.entity.crafting.IngredientOrder;
import stonering.entity.job.action.item.PutItemToContainerAction;
import stonering.entity.job.action.target.EntityActionTarget;
import stonering.entity.Entity;
import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.entity.crafting.ItemOrder;
import stonering.entity.item.Item;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.item.ItemContainer;
import stonering.generators.items.ItemGenerator;
import stonering.util.global.Logger;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;

/**
 * Action for crafting item by item order on workbench. Ingredient items will be brought to WB.
 * WB should have {@link WorkbenchAspect} and {@link ItemContainerAspect}.
 *
 * @author Alexander on 06.01.2019.
 */
public class CraftItemAction extends ItemConsumingAction {
    private ItemOrder itemOrder;
    private Entity workbench;

    public CraftItemAction(ItemOrder itemOrder, Entity workbench) {
        super(new EntityActionTarget(workbench, ActionTargetTypeEnum.NEAR)); // unit will stand near wb while performing task
        this.itemOrder = itemOrder;
        this.workbench = workbench;
        ItemContainerAspect containerAspect = workbench.get(ItemContainerAspect.class);
        WorkbenchAspect workbenchAspect = workbench.get(WorkbenchAspect.class);
        FuelConsumerAspect fuelAspect = workbench.get(FuelConsumerAspect.class);

        //Checks that action conditions are met. Creates sub action otherwise.
        //TODO check ingredients and fuel availability before bringing something to workbench.
        //TODO add usage of items in nearby containers.
        startCondition = () -> {
            if (workbenchAspect == null)
                return Logger.TASKS.logWarn("Building " + workbench.toString() + " is not a workbench.", FAIL);
            ActionConditionStatusEnum orderCheckResult = checkIngredientItems(containerAspect);
            if (orderCheckResult != OK) return orderCheckResult;
            if (fuelAspect != null && !fuelAspect.isFueled()) { // workbench requires fuel
                task.addFirstPreAction(new FuelingAciton(workbench));
                return NEW;
            }
            return OK;
        };

        // Creates item, consumes ingredients. Product item is put to Workbench.
        onFinish = () -> {
            ItemContainer container = GameMvc.model().get(ItemContainer.class);
            Item product = new ItemGenerator().generateItemByOrder(itemOrder);
            // spend components
            List<Item> items = itemOrder.getAllIngredients().stream()
                    .map(ingredientOrder -> ingredientOrder.items)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            items.forEach(item -> container.containedItemsSystem.removeItemFromContainer(item, containerAspect));
            container.removeItems(items);
            // put product into wb
            container.addItem(product);
            container.containedItemsSystem.addItemToContainer(product, containerAspect);
        };
    }

    /**
     * Checks that all ingredients have selected items.
     * Creates actions for bringing items to WB. Clears ingredients with 'spoiled' items.
     * Returns fail, if item for some ingredient cannot be found.
     */
    private ActionConditionStatusEnum checkIngredientItems(ItemContainerAspect containerAspect) {
        for (IngredientOrder allIngredient : itemOrder.getAllIngredients()) {

        }
        itemOrder.getAllIngredients().forEach(order -> {
            order.items.stream()
                    .filter(order.itemSelector::checkItem)
                    .containerAspect


            for (int i = 0; i < order.items.size(); i++) {
                Item item = order.items.iterator().next();

                if (item != null && order.itemSelector.checkItem(item) && aspect.containedItems.contains(item))
                    continue; // item ok

                if (item != null)
                    System.out.println("'spoiled' item in ingredient order"); // free item TODO locking items in container
                item = GameMvc.model().get(ItemContainer.class).util.getItemForIngredient(order, task.performer.position);
                if (item == null) return FAIL; // no valid item found
                order.items.set(i, item);
                if (aspect.containedItems.contains(item)) continue; // item in WB, no actions required

                task.addFirstPreAction(new PutItemToContainerAction(aspect.entity.get(ItemContainerAspect.class), item)); // create action to bring item
                return NEW; // new action is created
            }
        }
        return OK; // all ingredients have valid items
    }

    private void updateItems() {
        List<IngredientOrder> invalidIngredients = itemOrder.parts.values().stream() // find invalid ingredients
                .filter(ingredientOrder -> !ingredientOrderValid(ingredientOrder))
                .collect(Collectors.toList());
        invalidIngredients.forEach(this::clearIngredientItems); // clear all invalid ingredients
        for (IngredientOrder invalidOrder : invalidIngredients) {
            if (!findItemsForIngredient(invalidOrder)) return false; // no items found for some ingredient
        }
        return true;
    }

    private boolean ingredientOrderValid(IngredientOrder ingredientOrder) {
        LocalMap map = GameMvc.model().get(LocalMap.class);
        byte performerArea = map.passageMap.area.get(task.performer.position);
        return ingredientOrder.items.stream()
                .filter(item -> ingredientOrder.itemSelector.checkItem(item)) // item is still ok
                .map(item -> item.position)
                .map(map.passageMap.area::get)
                .filter(area -> area == performerArea) // item is still reachable
                .count() == ingredientOrder.ingredient.quantity;
    }

    private void clearIngredientItems

    @Override
    public String toString() {
        return "Crafting action: " + itemOrder.toString();
    }
}
