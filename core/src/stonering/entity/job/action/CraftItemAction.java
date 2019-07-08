package stonering.entity.job.action;

import stonering.entity.job.action.target.AspectHolderActionTarget;
import stonering.entity.Entity;
import stonering.entity.PositionAspect;
import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.entity.crafting.ItemOrder;
import stonering.entity.crafting.ItemPartOrder;
import stonering.entity.item.Item;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.game.GameMvc;
import stonering.game.model.lists.ItemContainer;
import stonering.generators.items.ItemGenerator;
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
        super(new AspectHolderActionTarget(workbench, true, false));
        desiredItems = new ArrayList<>();
        this.itemOrder = itemOrder;
        this.workbench = workbench;
    }

    @Override
    protected void performLogic() {
        Item product = new ItemGenerator().generateItem(itemOrder);
        ItemContainerAspect workbenchContainer = workbench.getAspect(ItemContainerAspect.class);
        workbenchContainer.getItems().removeAll(desiredItems); // spend components
        workbenchContainer.getItems().add(product); // add product
    }

    /**
     * Checks that name conditions are met. Creates sub name otherwise.
     */
    @Override
    public int check() {
        ItemContainerAspect containerAspect = workbench.getAspect(ItemContainerAspect.class); //TODO remove item container requirement (item in unit inventory, on the ground or in !nearby containers!).
        if (workbench.getAspect(WorkbenchAspect.class) == null || containerAspect == null) {
            Logger.TASKS.logWarn("Building " + workbench.toString() + " is not a workbench with item container.");
            return FAIL;
        }
        if (!updateDesiredItems()) return FAIL; // desiredItems valid after this
        if (!containerAspect.getItems().containsAll(desiredItems)) { // some item are out of WB.
            List<Item> outOfWBItems = new ArrayList<>(desiredItems);
            outOfWBItems.removeAll(containerAspect.getItems());
            task.addFirstPreAction(new ItemPutAction(outOfWBItems.get(0), workbench)); // create name to bring item
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
        if (desiredItems.isEmpty() || !GameMvc.instance().getModel().get(ItemContainer.class).checkItemList(desiredItems)) {
            return findDesiredItems();
        }
        return true;
    }

    /**
     * Searches desiredItems for each order part. Returns false if no desiredItems for order part found.
     */
    private boolean findDesiredItems() {
        List<Item> uncheckedItems = new ArrayList<>();
        uncheckedItems.addAll(desiredItems);
        for (ItemPartOrder part : itemOrder.getParts()) {
            List<Item> foundItems = GameMvc.instance().getModel().get(ItemContainer.class).getItemsAvailableBySelector(part.getItemSelector(), workbench.getAspect(PositionAspect.class).position);
            if (foundItems.isEmpty()) {
                desiredItems.clear();
                return false;
            }
            //TODO add amount
            foundItems.removeAll(desiredItems);
            desiredItems.addAll(GameMvc.instance().getModel().get(ItemContainer.class).getNearestItems(foundItems, 1));
        }
        return true;
    }

    @Override
    public String toString() {
        return "Crafting name: " + itemOrder.toString();
    }
}
