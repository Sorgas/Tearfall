package stonering.entity.jobs.actions;

import stonering.entity.jobs.Task;
import stonering.entity.local.AspectHolder;
import stonering.entity.local.building.aspects.WorkbenchAspect;
import stonering.entity.local.crafting.ItemOrder;
import stonering.entity.local.crafting.ItemPartOrder;
import stonering.entity.local.items.Item;
import stonering.entity.local.items.aspects.ItemContainerAspect;
import stonering.game.core.model.GameContainer;
import stonering.generators.items.ItemGenerator;
import stonering.global.utils.Position;
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
    private Task task;
    private ItemOrder itemOrder;
    private AspectHolder workbench;
    private List<Item> desiredItems; // these items should be in WB.
    private Item tool; //TODO

    public CraftItemAction(GameContainer gameContainer, ItemOrder itemOrder, AspectHolder workbench) {
        super(gameContainer);
        desiredItems = new ArrayList<>();
        this.itemOrder = itemOrder;
        this.workbench = workbench;
    }

    @Override
    public boolean doLogic() {
        Item product = new ItemGenerator().generateItem(itemOrder);
        ((ItemContainerAspect) workbench.getAspects().get(ItemContainerAspect.NAME)).getItems().add(product);
        return true;
    }

    /**
     * Checks that action conditions are met. Creates sub actions otherwise.
     */
    @Override
    public boolean check() {
        if (workbench.getAspects().get(WorkbenchAspect.NAME) == null) {
            TagLoggersEnum.TASKS.logWarn("Building " + workbench.toString() + " is not a workbench.");
            return false;
        }
        ItemContainerAspect containerAspect = (ItemContainerAspect) workbench.getAspects().get(ItemContainerAspect.NAME);
        if (containerAspect == null) {
            TagLoggersEnum.TASKS.logWarn("Building " + workbench.toString() + " is not a container.");
            return false;
        }
        if (!updateDesiredItems()) return false; // desiredItems valid after this
        if (!containerAspect.getItems().containsAll(desiredItems)) { // some items are out of WB.
            List<Item> outOfWBItems = new ArrayList<>(desiredItems);
            outOfWBItems.removeAll(containerAspect.getItems());
            task.addFirstPreAction(new ItemPutAction(gameContainer, outOfWBItems.get(0), workbench)); // create action to bring item
        }
        return true;
    }

    @Override
    public Position getTargetPosition() {
        return workbench.getPosition();
    }

    /**
     * Checks that desired items are still valid or tries to find new ones.
     *
     * @return true, if items exist or found.
     */
    private boolean updateDesiredItems() {
        if (desiredItems.isEmpty() || !gameContainer.getItemContainer().checkItemList(desiredItems)) {
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
            List<Item> foundItems = gameContainer.getItemContainer().getItemsAvailableBySelector(part.getSelected(), workbench.getPosition());
            if (foundItems.isEmpty()) {
                desiredItems.clear();
                return false;
            }
            //TODO add amount
            foundItems.removeAll(desiredItems);
            desiredItems.addAll(gameContainer.getItemContainer().getNearestItems(foundItems, 1));
        }
        return true;
    }
}
