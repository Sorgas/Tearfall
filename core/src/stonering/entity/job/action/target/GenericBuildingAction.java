package stonering.entity.job.action.target;

import stonering.entity.item.Item;
import stonering.entity.item.selectors.ItemSelector;
import stonering.entity.job.action.ItemConsumingAction;
import stonering.entity.job.action.PutItemAction;
import stonering.entity.job.designation.BuildingDesignation;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.items.ItemContainer;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static stonering.entity.job.action.target.ActionTarget.ANY;
import static stonering.entity.job.action.target.ActionTarget.NEAR;
import static stonering.enums.blocks.BlockTypesEnum.NOT_PASSABLE;

/**
 * Action for creating constructions and buildings on map.
 * Creates action for bringing materials to and removing all other items from construction site.
 *
 * @author Alexander on 02.10.2019.
 */
public abstract class GenericBuildingAction extends ItemConsumingAction {

    protected GenericBuildingAction(BuildingDesignation designation, Collection<ItemSelector> itemSelectors) {
        super(new PositionActionTarget(designation.getPosition(), BlockTypesEnum.getType(designation.getBuilding()).PASSING == NOT_PASSABLE ? NEAR : ANY));
        selectors = new ArrayList<>(itemSelectors);
    }

    @Override
    protected List<Item> getAvailableItems() {
        List<Item> items = new ArrayList<>(GameMvc.instance().getModel().get(ItemContainer.class).getItemsInPosition(actionTarget.getPosition())); //TODO checkItems positions near target.
        items.addAll(task.getPerformer().getAspect(EquipmentAspect.class).hauledItems); // from performer inventory
        return items;
    }

    /**
     * Checks that all material selectors have corresponding item in building position or in performer's inventory.
     * Creates action for bringing missing item to target position.
     * All missing items will be dropped as it's somewhat tough to count last one.
     * Creates action for removing blocking items from target position
     */
    @Override
    public int check() {
        Logger.TASKS.log("Checking " + this);
        List<Item> availableItems = getAvailableItems();
        List<Item> itemsOnSite = GameMvc.instance().getModel().get(ItemContainer.class).getItemsInPosition(actionTarget.getPosition());
        for (ItemSelector itemSelector : selectors) {
            List<Item> selectedItems = itemSelector.selectItems(availableItems);
            if (selectedItems.isEmpty()) return tryCreateBringingAction(itemSelector); // some selector has no item
            availableItems.removeAll(selectedItems);
            itemsOnSite.removeAll(selectedItems);
        }
        // create action, if target position is not free
        return itemsOnSite.isEmpty() ? OK : createSiteClearingAction(itemsOnSite.get(0));
    }

    /**
     * Creates action for putting item to target position and adds it to task.
     *
     * @param itemSelector selector for item
     * @return FAIL if no item available.
     */
    private int tryCreateBringingAction(ItemSelector itemSelector) {
        Position position = actionTarget.getPosition();
        ItemContainer itemContainer = GameMvc.instance().getModel().get(ItemContainer.class);
        if (!itemContainer.hasItemsAvailableBySelector(itemSelector, position)) return FAIL;
        Item item = itemContainer.getItemAvailableBySelector(itemSelector, position);
        if (item == null) return FAIL;
        PutItemAction putItemAction = new PutItemAction(item, position);
        task.addFirstPreAction(putItemAction);
        return NEW;
    }

    /**
     * Creates {@link PutItemAction} for placing blocking item out of target position(to neighbour one).
     */
    private int createSiteClearingAction(Item item) {
        LocalMap localMap = GameMvc.instance().getModel().get(LocalMap.class);
        PutItemAction putItemAction = new PutItemAction(item, localMap.getAnyNeighbourPosition(actionTarget.getPosition(), BlockTypesEnum.PASSABLE));
        task.addFirstPreAction(putItemAction);
        return NEW;
    }
}
