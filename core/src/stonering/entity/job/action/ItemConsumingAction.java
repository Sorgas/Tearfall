package stonering.entity.job.action;

import stonering.entity.item.Item;
import stonering.entity.item.selectors.ItemSelector;
import stonering.entity.job.action.target.ActionTarget;
import stonering.game.GameMvc;
import stonering.game.model.system.ItemContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic action for all actions that consume some items in the process.
 * Method to determine what actions can be consumed should be provided in subclasses.
 * Selection for consuming and removing from container are split,
 * as some information about seleced items may be needed (e.g. material for building, food quality for eating).
 *
 * @author Alexander on 02.10.2019.
 */
public abstract class ItemConsumingAction extends Action {
    protected List<ItemSelector> selectors;

    protected ItemConsumingAction(ActionTarget actionTarget) {
        super(actionTarget);
    }

    protected abstract List<Item> getAvailableItems();

    /**
     * Selects items from available ones by selectors.
     * Returns empty list if some selectors have no items.
     */
    protected List<Item> selectItemsToConsume() {
        List<Item> availableItems = getAvailableItems();
        List<Item> itemsToConsume = new ArrayList<>();
        for (ItemSelector itemSelector : selectors) {
            List<Item> selectedItems = itemSelector.selectItems(availableItems);
            if(selectedItems.isEmpty()) return new ArrayList<>();
            itemsToConsume.addAll(selectedItems);
            availableItems.removeAll(selectedItems);
        }
        return itemsToConsume;
    }

    /**
     * Removes items from game.
     */
    protected void consumeItems(List<Item> items) {
        GameMvc.instance().getModel().get(ItemContainer.class).removeItems(items);
    }
}
