package stonering.entity.jobs.actions;

import stonering.entity.jobs.actions.target.AspectHolderActionTarget;
import stonering.entity.local.AspectHolder;
import stonering.entity.local.building.aspects.WorkbenchAspect;
import stonering.entity.local.crafting.ItemOrder;
import stonering.entity.local.crafting.ItemPartOrder;
import stonering.entity.local.items.Item;
import stonering.entity.local.items.aspects.ItemContainerAspect;
import stonering.game.core.model.lists.ItemContainer;
import stonering.generators.items.ItemGenerator;
import stonering.util.global.TagLoggersEnum;

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
    private AspectHolder workbench;
    private List<Item> desiredItems; // these items should be in WB.
    private Item tool; //TODO

    public CraftItemAction(ItemOrder itemOrder, AspectHolder workbench) {
        super(new AspectHolderActionTarget(workbench, true, false));
        desiredItems = new ArrayList<>();
        this.itemOrder = itemOrder;
        this.workbench = workbench;
    }

    @Override
    protected void performLogic() {
        Item product = new ItemGenerator().generateItem(itemOrder);
        ItemContainerAspect workbenchContainer = ((ItemContainerAspect) workbench.getAspects().get(ItemContainerAspect.NAME));
        workbenchContainer.getItems().removeAll(desiredItems); // spend components
        workbenchContainer.getItems().add(product); // add product
    }

    /**
     * Checks that action conditions are met. Creates sub actions otherwise.
     */
    @Override
    public boolean check() {
        ItemContainerAspect containerAspect = (ItemContainerAspect) workbench.getAspects().get(ItemContainerAspect.NAME); //TODO remove item container requirement (items in units inventory, on the ground or in !nearby containers!).
        if (workbench.getAspects().get(WorkbenchAspect.NAME) == null || containerAspect == null) {
            TagLoggersEnum.TASKS.logWarn("Building " + workbench.toString() + " is not a workbench with item container.");
            return false;
        }
        if (!updateDesiredItems()) return false; // desiredItems valid after this
        if (!containerAspect.getItems().containsAll(desiredItems)) { // some items are out of WB.
            List<Item> outOfWBItems = new ArrayList<>(desiredItems);
            outOfWBItems.removeAll(containerAspect.getItems());
            task.addFirstPreAction(new ItemPutAction(outOfWBItems.get(0), workbench)); // create action to bring item
        }
        return true;
    }

    /**
     * Checks that desired items are still valid or tries to find new ones.
     *
     * @return true, if items exist or found.
     */
    private boolean updateDesiredItems() {
        if (desiredItems.isEmpty() || !gameMvc.getModel().get(ItemContainer.class).checkItemList(desiredItems)) {
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
            List<Item> foundItems = gameMvc.getModel().get(ItemContainer.class).getItemsAvailableBySelector(part.getSelected(), workbench.getPosition());
            if (foundItems.isEmpty()) {
                desiredItems.clear();
                return false;
            }
            //TODO add amount
            foundItems.removeAll(desiredItems);
            desiredItems.addAll(gameMvc.getModel().get(ItemContainer.class).getNearestItems(foundItems, 1));
        }
        return true;
    }

    @Override
    public String toString() {
        return "Crafting action: " + itemOrder.toString();
    }
}
