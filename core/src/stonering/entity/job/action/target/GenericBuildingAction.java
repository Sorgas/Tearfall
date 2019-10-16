package stonering.entity.job.action.target;

import stonering.entity.building.BuildingType;
import stonering.entity.item.Item;
import stonering.entity.item.selectors.ItemSelector;
import stonering.entity.job.action.ItemConsumingAction;
import stonering.entity.job.action.PutItemAction;
import stonering.entity.job.designation.BuildingDesignation;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.buildings.BuildingTypeMap;
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
        super(createTarget(designation));
        selectors = new ArrayList<>(itemSelectors);
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
        for (ItemSelector itemSelector : selectors) {
            List<Item> selectedItems = itemSelector.selectItems(items);
            if (selectedItems.isEmpty()) return tryCreateBringingAction(itemSelector); // some selector has no items
            items.removeAll(selectedItems);
        }
        // create action, if target position is not free from non-selected items
        return items.isEmpty() ? OK : createSiteClearingAction(items.get(0));
    }

    @Override
    protected List<Item> getAvailableItems() {
        return new ArrayList<>(GameMvc.instance().getModel().get(ItemContainer.class).getItemsInPosition(actionTarget.getPosition()));
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
        ItemContainer itemContainer = GameMvc.instance().getModel().get(ItemContainer.class);
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
        LocalMap localMap = GameMvc.instance().getModel().get(LocalMap.class);
        PutItemAction putItemAction = new PutItemAction(item, localMap.getAnyNeighbourPosition(actionTarget.getPosition(), BlockTypesEnum.PASSABLE));
        task.addFirstPreAction(putItemAction);
        return NEW;
    }

    private static ActionTarget createTarget(BuildingDesignation designation) {
        BuildingType type = BuildingTypeMap.instance().getBuilding(designation.building);
        return new PositionActionTarget(designation.position, BlockTypesEnum.getType(type.passage).PASSING == NOT_PASSABLE ? NEAR : ANY);
    }
}
