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
import stonering.util.geometry.Position;
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
            if (workbenchAspect == null) return Logger.TASKS.logWarn("Building " + workbench.toString() + " is not a workbench.", FAIL);
            if (!ingredientOrdersValid()) {
                return FAIL;
            }
//            ActionConditionStatusEnum orderCheckResult = checkIngredientItems(containerAspect);
//            if (orderCheckResult != OK) return orderCheckResult;
//            if (fuelAspect != null && !fuelAspect.isFueled()) { // workbench requires fuel
//                task.addFirstPreAction(new FuelingAciton(workbench));
//                return NEW;
//            }
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

    @Override
    protected Position getPositionForItems() {
        return workbench.position;
    }

    @Override
    protected void createBringingAction(Item item) {
        task.addFirstPreAction(new PutItemToContainerAction(workbench.get(ItemContainerAspect.class), item));
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

    @Override
    public String toString() {
        return "Crafting action: " + itemOrder.toString();
    }
}
