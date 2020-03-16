package stonering.entity.job.action;

import stonering.entity.building.aspects.FuelConsumerAspect;
import stonering.entity.crafting.IngredientOrder;
import stonering.entity.job.action.equipment.PutItemAction;
import stonering.entity.job.action.target.EntityActionTarget;
import stonering.entity.Entity;
import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.entity.crafting.ItemOrder;
import stonering.entity.item.Item;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;
import stonering.generators.items.ItemGenerator;
import stonering.util.global.Logger;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;

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

        //Checks that action conditions are met. Creates sub action otherwise.
        //TODO check ingredients and fuel availability before bringing something to workbench.
        //TODO add usage of items in nearby containers.
        startCondition = () -> {
            WorkbenchAspect aspect = workbench.get(WorkbenchAspect.class);
            if (workbench.get(WorkbenchAspect.class) == null)
                return Logger.TASKS.logWarn("Building " + workbench.toString() + " is not a workbench.", FAIL);
            ActionConditionStatusEnum orderCheckResult = checkIngredientItems(aspect);
            if (orderCheckResult != OK) return orderCheckResult;
            if (workbench.has(FuelConsumerAspect.class) && !workbench.get(FuelConsumerAspect.class).isFueled()) { // workbench requires fuel
                task.addFirstPreAction(new FuelingAciton(workbench));
                return NEW;
            }
            return OK;
        };

        // Creates item, consumes ingredients. Product item is put to Workbench.
        onFinish = () -> {
            ItemContainer container = GameMvc.instance().model().get(ItemContainer.class);
            WorkbenchAspect workbenchAspect = workbench.get(WorkbenchAspect.class);
            Item product = new ItemGenerator().generateItemByOrder(itemOrder);
            // spend components
            List<Item> items = itemOrder.getAllIngredients().stream()
                    .map(ingredientOrder -> ingredientOrder.items)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            container.containedItemsSystem.removeItemsFromWorkbench(items, workbenchAspect);
            container.removeItems(items);
            // put product into wb
            container.addItem(product);
            container.containedItemsSystem.addItemToWorkbench(product, workbenchAspect);
        };
    }

    /**
     * Checks that all ingredients have selected items.
     * Creates actions for bringing items to WB. Clears ingredients with 'spoiled' items.
     * Returns fail, if item for some ingredient cannot be found.
     */
    private ActionConditionStatusEnum checkIngredientItems(WorkbenchAspect aspect) {
        for (IngredientOrder order : itemOrder.getAllIngredients()) {
            for (int i = 0; i < order.items.size(); i++) {
                Item item = order.items.get(i);
                if (item != null && order.itemSelector.checkItem(item) && aspect.containedItems.contains(item)) continue; // item ok
                if (item != null) System.out.println("'spoiled' item in ingredient order"); // free item TODO locking items in container
                item = GameMvc.model().get(ItemContainer.class).util.getItemForIngredient(order, task.performer.position);
                if (item == null) return FAIL; // no valid item found
                order.items.set(i, item);
                if (aspect.containedItems.contains(item)) continue; // item in WB, no actions required
                task.addFirstPreAction(new PutItemAction(item, workbench)); // create action to bring item
                return NEW; // new action is created
            }
        }
        return OK; // all ingredients have valid items
    }

    @Override
    public String toString() {
        return "Crafting action: " + itemOrder.toString();
    }
}

