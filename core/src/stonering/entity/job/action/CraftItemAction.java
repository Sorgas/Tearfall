package stonering.entity.job.action;

import stonering.entity.building.aspects.FuelConsumerAspect;
import stonering.entity.crafting.IngredientOrder;
import stonering.entity.job.action.target.ActionTarget;
import stonering.entity.job.action.target.EntityActionTarget;
import stonering.entity.Entity;
import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.entity.crafting.ItemOrder;
import stonering.entity.item.Item;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.generators.items.ItemGenerator;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.List;

import static stonering.enums.blocks.BlockTypesEnum.PassageEnum.PASSABLE;

/**
 * Action for crafting item by item order on workbench. Items for crafting will be brought to WB.
 * WB should have {@link WorkbenchAspect} and {@link ItemContainerAspect}.
 *
 * @author Alexander on 06.01.2019.
 */
public class CraftItemAction extends Action {
    private ItemOrder itemOrder;
    private Entity workbench;
    private List<Item> desiredItems; // these item should be in WB.
    private Item tool; //TODO

    public CraftItemAction(ItemOrder itemOrder, Entity workbench) {
        super(new EntityActionTarget(workbench, ActionTarget.NEAR)); // unit will stand near wb while performing task
        desiredItems = new ArrayList<>();
        this.itemOrder = itemOrder;
        this.workbench = workbench;
    }

    /**
     * Creates item, consumes ingredients.
     * Puts item into workbench, if it has {@link ItemContainerAspect}, or on the ground near.
     */
    @Override
    protected void performLogic() {
        ItemContainerAspect workbenchContainer = workbench.getAspect(ItemContainerAspect.class);
        ItemContainer container = GameMvc.instance().getModel().get(ItemContainer.class);
        Item product = new ItemGenerator().generateItemByOrder(null, itemOrder);
        if (workbenchContainer != null) {
            workbenchContainer.items.removeAll(desiredItems); // spend components
            container.addItem(product);
            workbenchContainer.items.add(product); // put product into wb
            container.itemAddedToContainer(product, workbenchContainer);
        } else {
            //TODO
            workbenchContainer.items.removeAll(desiredItems); // spend components
            container.removeItems(desiredItems);

            product.position = GameMvc.instance().getModel().get(LocalMap.class).getAnyNeighbourPosition(workbench.position, PASSABLE);
            container.addAndPut(product);
        }
    }

    /**
     * Checks that action conditions are met. Creates sub action otherwise.
     * TODO check ingredients and fuel availability before bringing something to workbench
     */
    @Override
    public int check() {
        //TODO add usage of items in unit inventory, on the ground or in !nearby containers!).
        WorkbenchAspect aspect = workbench.getAspect(WorkbenchAspect.class);
        if (workbench.getAspect(WorkbenchAspect.class) == null)
            return Logger.TASKS.logWarn("Building " + workbench.toString() + " is not a workbench.", FAIL);
        if (!updateDesiredItems()) return FAIL; // desiredItems valid after this

        if (!aspect.containedItems.containsAll(desiredItems)) { // some item are out of WB.
            List<Item> outOfWBItems = new ArrayList<>(desiredItems);
            outOfWBItems.removeAll(aspect.containedItems);
            task.addFirstPreAction(new PutItemAction(outOfWBItems.get(0), workbench)); // create action to bring item
            return NEW;
        }
        if (workbench.hasAspect(FuelConsumerAspect.class) && !workbench.getAspect(FuelConsumerAspect.class).isFueled()) { // workbench requires fuel
            task.addFirstPreAction(new FuelingAciton(workbench));
            return NEW;
        }
        return OK;
    }

    /**
     * Checks that desired item are still valid or tries to find new ones.
     *
     * @return true, if item exist or found.
     */
    private boolean updateDesiredItems() {
        if (desiredItems.isEmpty() || !checkItemsAvailability(desiredItems)) { // items are not yet searched on map, or was removed from map
            return findDesiredItems();
        }
        return true;
    }

    /**
     * Searches desiredItems for each order part.
     * Returns false if cannot find any items for at least one order part.
     */
    private boolean findDesiredItems() {
        ItemContainer container = GameMvc.instance().getModel().get(ItemContainer.class);
        desiredItems.clear();
        List<IngredientOrder> ingredientOrders = new ArrayList<>(itemOrder.parts.values());
        ingredientOrders.addAll(itemOrder.consumed);
        for (IngredientOrder ingredientOrder : ingredientOrders) {
            List<Item> foundItems = container.util.getItemsAvailableBySelector(ingredientOrder.itemSelector, task.performer.position);
            foundItems.removeAll(desiredItems); // remove already added items
            if (foundItems.isEmpty()) { // no items found for ingredient
                desiredItems.clear();
                return false;
            }
            desiredItems.addAll(container.util.getNearestItems(foundItems, task.performer.position, 1)); // add nearest items to order
        }
        return true;
    }

    private boolean checkItemsAvailability(List<Item> items) {
        ItemContainer container = GameMvc.instance().getModel().get(ItemContainer.class);
        return items.stream().allMatch(item -> container.util.itemIsAvailable(item, task.performer.position));
    }

    @Override
    public String toString() {
        return "Crafting action: " + itemOrder.toString();
    }
}
