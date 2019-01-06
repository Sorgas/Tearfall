package stonering.entity.jobs.actions;

import stonering.entity.jobs.Task;
import stonering.entity.local.building.Building;
import stonering.entity.local.building.aspects.WorkbenchAspect;
import stonering.entity.local.crafting.ItemOrder;
import stonering.entity.local.crafting.ItemPartOrder;
import stonering.entity.local.items.Item;
import stonering.entity.local.items.aspects.ItemContainerAspect;
import stonering.game.core.model.GameContainer;
import stonering.util.global.TagLoggersEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander on 06.01.2019.
 */
public class CraftItemAction extends Action {
    Task task;
    ItemOrder itemOrder;
    Building workbench;
    List<Item> items;

    public CraftItemAction(GameContainer gameContainer) {
        super(gameContainer);
    }

    /**
     * Checks that action conditions are met. Creates sub actions otherwise.
     */
    public boolean check() {
        if (workbench.getAspects().get(WorkbenchAspect.NAME) == null) {
            TagLoggersEnum.TASKS.logWarn("Building " + workbench.getName() + " is not a workbench.");
            return false;
        }
        if (workbench.getAspects().get(ItemContainerAspect.NAME) == null) {
            TagLoggersEnum.TASKS.logWarn("Building " + workbench.getName() + " is not a container.");
            return false;
        }
        if ((items == null || !checkDesiredItems()) && !findDesiredItems()) return false;
        List<Item> storedItems = ((ItemContainerAspect) workbench.getAspects().get(ItemContainerAspect.NAME)).getItems();
        if (!storedItems.containsAll(items)) {
            List<Item> copyList = new ArrayList<>();
            copyList.addAll(items);
            copyList.removeAll(storedItems);
            createSubActionForItem(copyList.get(0));
        }
        return true;
    }

    public boolean perform() {
        return false
    }

    private void createSubActionForItem(Item item) {
        Action carryAction = new Action();
        task.addFirstPreAction(carryAction);
    }

    private boolean checkDesiredItems() {
        List<Item> uncheckedItems = new ArrayList<>();
        uncheckedItems.addAll(items);
        for (ItemPartOrder part : itemOrder.getParts()) {
            List<Item> checkedItems = part.getSelected().selectItems(uncheckedItems);
            if (checkedItems.isEmpty()) {
                TagLoggersEnum.TASKS.logWarn("No items found for " + part.getName());
                return false;
            }
            uncheckedItems.removeAll(checkedItems);
        }
        if (!uncheckedItems.isEmpty()) {
            TagLoggersEnum.TASKS.logWarn("More than required items have been selected for order " + itemOrder.getRecipe().getName());
        }
        return true;
    }

    private boolean findDesiredItems() {
        items = new ArrayList<>();
        List<Item> uncheckedItems = new ArrayList<>();
        uncheckedItems.addAll(items);
        for (ItemPartOrder part : itemOrder.getParts()) {
            List<Item> foundItems = gameContainer.getItemContainer().getItemsAvailableBySelector(part.getSelected(), workbench.getPosition());
            if (foundItems.isEmpty()) {
                //
                return false;
            }
            //TODO add amount
            items.addAll(gameContainer.getItemContainer().getNearestItems(foundItems, 1));
        }
        return true;
    }
}
