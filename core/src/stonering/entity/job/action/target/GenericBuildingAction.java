package stonering.entity.job.action.target;

import stonering.entity.building.BuildingOrder;
import stonering.entity.building.BuildingType;
import stonering.entity.item.Item;
import stonering.entity.item.selectors.ItemSelector;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.PutItemAction;
import stonering.entity.job.designation.BuildingDesignation;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.item.ItemContainer;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static stonering.enums.blocks.BlockTypesEnum.PassageEnum.IMPASSABLE;
import static stonering.enums.blocks.BlockTypesEnum.PassageEnum.PASSABLE;

/**
 * Action for creating constructions and buildings on map.
 * Target position should be clear from items.
 * Building can be created in the same z-level cell next to a builder,
 * or if builder can step into cell with construction after completing it (for constructions).
 *
 * Materials for construction should be brought to the cell where builder can stand.
 *
 * @author Alexander on 02.10.2019.
 */
public abstract class GenericBuildingAction extends Action {
    protected final BuildingOrder order;

    protected GenericBuildingAction(BuildingOrder order) {
        super(new PositionActionTarget(order.getPosition(), ActionTargetTypeEnum.NEAR));
        this.order = order;
    }

    /**
     * Checks that all material selectors have corresponding item in building position.
     * Creates action for bringing missing item to target position.
     * Creates action for removing blocking items from target position
     */
    @Override
    public int check() {
        Logger.TASKS.log("Checking " + this);
        List<Item> items = getAvailableItems();
        for (ItemSelector itemSelector : ) {
            List<Item> selectedItems = itemSelector.selectItems(items);
            if (selectedItems.isEmpty()) return tryCreateBringingAction(itemSelector); // some selector has no items
            items.removeAll(selectedItems);
        }
        // create action, if target position is not free from non-selected items
        return items.isEmpty() ? OK : createSiteClearingAction(items.get(0));
    }

    @Override
    protected List<Item> getAvailableItems() {
        return new ArrayList<>(GameMvc.instance().model().get(ItemContainer.class).getItemsInPosition(actionTarget.getPosition()));
    }

    /**
     * Creates action for putting item to target position and adds it to task.
     *
     * @param itemSelector selector for item
     * @return FAIL if no item available.
     */
    private int tryCreateBringingAction(ItemSelector itemSelector) {
        Logger.TASKS.logDebug("Creating action for bringing item.");
        Position position = actionTarget.getPosition();
        ItemContainer itemContainer = GameMvc.instance().model().get(ItemContainer.class);
        Item item = itemContainer.util.getItemAvailableBySelector(itemSelector, position);
        if (item == null) {
            Logger.TASKS.logDebug("No Item available.");
            return FAIL;
        }
        PutItemAction putItemAction = new PutItemAction(item, position);
        task.addFirstPreAction(putItemAction);
        Logger.TASKS.logDebug("Putting action created.");
        return NEW;
    }

    /**
     * Creates {@link PutItemAction} for placing blocking item out of target position(to neighbour one).
     */
    private int createSiteClearingAction(Item item) {
        LocalMap localMap = GameMvc.instance().model().get(LocalMap.class);
        PutItemAction putItemAction = new PutItemAction(item, localMap.getAnyNeighbourPosition(actionTarget.getPosition(), PASSABLE));
        task.addFirstPreAction(putItemAction);
        return NEW;
    }
}
