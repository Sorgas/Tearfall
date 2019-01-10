package stonering.entity.jobs.actions.aspects.requirements;

import stonering.entity.jobs.actions.Action;
import stonering.entity.jobs.actions.aspects.effect.PutItemFromInventoryToContainerEffectAspect;
import stonering.entity.jobs.actions.aspects.target.BuildingTargetAspect;
import stonering.entity.local.building.Building;
import stonering.entity.local.items.Item;
import stonering.entity.local.items.aspects.ItemContainerAspect;
import stonering.entity.local.items.selectors.ItemSelector;
import stonering.game.core.model.lists.ItemContainer;
import stonering.util.geometry.Position;
import stonering.util.global.TagLoggersEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks that specified items are in building container.
 */
public class ItemsInBuildingRequirementAspect extends RequirementsAspect {
    private Building building; //TODO generalize to AspectHolder
    private List<ItemSelector> itemSelectors;

    public ItemsInBuildingRequirementAspect(Action action, Building building) {
        super(action);
        itemSelectors = new ArrayList<>();
        this.building = building;
    }

    /**
     * Checks if all item selectors have corresponding items in building.
     * Creates sub action if some have not.
     */
    @Override
    public boolean check() {
        if (!building.getAspects().containsKey(ItemContainerAspect.NAME)) {
            TagLoggersEnum.TASKS.logDebug("Target building is not container.");
            return false;
        }
        List<Item> buildingItems = ((ItemContainerAspect) building.getAspects().get(ItemContainerAspect.NAME)).getItems();
        List<Item> itemsForSearch = new ArrayList<>(buildingItems);
        for (ItemSelector itemSelector : itemSelectors) {
            List<Item> selectedItems = itemSelector.selectItems(itemsForSearch);
            if (!selectedItems.isEmpty()) {
                itemsForSearch.removeAll(selectedItems);
            } else {
                return createSubAction(itemSelector);
            }
        }
        return true;
    }

    /**
     * Creates action for putting item to container, or fails task if no items available.
     */
    private boolean createSubAction(ItemSelector itemSelector) {
        TagLoggersEnum.TASKS.logDebug("Creating action for filling workbench.");
        Position target = action.getTargetPosition();
        ItemContainer itemContainer = action.getGameContainer().getItemContainer();
        Item item = itemContainer.getItemAvailableBySelector(itemSelector, target);
        if (item != null) {
            Action putAction = new Action(action.getGameContainer());
            putAction.setRequirementsAspect(new ItemInInventoryRequirementAspect(putAction, itemSelector));
            putAction.setTargetAspect(new BuildingTargetAspect(putAction, false, true, building));
            putAction.setEffectAspect(new PutItemFromInventoryToContainerEffectAspect(putAction, 10, item));
            action.getTask().addFirstPreAction(putAction);
            return true;
        }
        return false;
    }
}
