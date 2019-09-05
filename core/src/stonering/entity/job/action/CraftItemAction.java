package stonering.entity.job.action;

import stonering.entity.crafting.IngredientOrder;
import stonering.entity.job.action.target.ActionTarget;
import stonering.entity.job.action.target.EntityActionTarget;
import stonering.entity.Entity;
import stonering.entity.PositionAspect;
import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.entity.crafting.ItemOrder;
import stonering.entity.item.Item;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.lists.ItemContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.generators.items.ItemGenerator;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Action for crafting item by item order on workbench. Items for crafting will be brought to WB.
 * WB should have {@link WorkbenchAspect} and {@link ItemContainerAspect}
 *
 * @author Alexander on 06.01.2019.
 */
public class CraftItemAction extends Action {
    private ItemOrder itemOrder;
    private Entity workbench;
    private List<Item> desiredItems; // these item should be in WB.
    private Item tool; //TODO

    public CraftItemAction(ItemOrder itemOrder, Entity workbench) {
        super(new EntityActionTarget(workbench, ActionTarget.EXACT));
        desiredItems = new ArrayList<>();
        this.itemOrder = itemOrder;
        this.workbench = workbench;
    }

    @Override
    protected void performLogic() {
        Position targetPosition = GameMvc.instance().getModel().get(LocalMap.class).getAnyNeighbourPosition(workbench.position, BlockTypesEnum.PASSABLE);
        Item product = new ItemGenerator().generateItemByOrder(targetPosition, itemOrder);
        ItemContainerAspect workbenchContainer = workbench.getAspect(ItemContainerAspect.class);
        workbenchContainer.items.removeAll(desiredItems); // spend components
        GameMvc.instance().getModel().get(ItemContainer.class).addItem(product);
    }

    /**
     * Checks that action conditions are met. Creates sub action otherwise.
     */
    @Override
    public int check() {
        ItemContainerAspect containerAspect = workbench.getAspect(ItemContainerAspect.class); //TODO remove item container requirement (item in unit inventory, on the ground or in !nearby containers!).
        if (workbench.getAspect(WorkbenchAspect.class) == null || containerAspect == null) {
            Logger.TASKS.logWarn("Building " + workbench.toString() + " is not a workbench with item container.");
            return FAIL;
        }
        if (!updateDesiredItems()) return FAIL; // desiredItems valid after this
        if (!containerAspect.items.containsAll(desiredItems)) { // some item are out of WB.
            List<Item> outOfWBItems = new ArrayList<>(desiredItems);
            outOfWBItems.removeAll(containerAspect.items);
            task.addFirstPreAction(new ItemPutAction(outOfWBItems.get(0), workbench)); // create action to bring item
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
        if (desiredItems.isEmpty() || !GameMvc.instance().getModel().get(ItemContainer.class).checkItemList(desiredItems)) { // items are not yet searched on map, or was 000000removed from map
            return findDesiredItems();
        }
        return true;
    }

    /**
     * Searches desiredItems for each order part. Returns false if no desiredItems for order part found.
     */
    private boolean findDesiredItems() {
        ItemContainer container = GameMvc.instance().getModel().get(ItemContainer.class);
        desiredItems.clear();
        List<IngredientOrder> ingredientOrders = new ArrayList<>(itemOrder.parts.values());
        ingredientOrders.addAll(itemOrder.consumed);
        for (IngredientOrder ingredientOrder : ingredientOrders) {
            List<Item> foundItems = container.getItemsAvailableBySelector(ingredientOrder.itemSelector, workbench.getAspect(PositionAspect.class).position);
            foundItems.removeAll(desiredItems); // remove already added items
            if (foundItems.isEmpty()) { // no items found for ingredient
                desiredItems.clear();
                return false;
            }
            desiredItems.addAll(container.getNearestItems(foundItems, 1)); // add nearest items to order
        }
        return true;
    }

    @Override
    public String toString() {
        return "Crafting action: " + itemOrder.toString();
    }
}
