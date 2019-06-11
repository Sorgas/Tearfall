package stonering.entity.job.action;

import stonering.entity.job.designation.BuildingDesignation;
import stonering.entity.job.action.target.PositionActionTarget;
import stonering.entity.local.items.Item;
import stonering.entity.local.items.selectors.ItemSelector;
import stonering.entity.local.unit.aspects.equipment.EquipmentAspect;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.lists.ItemContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Action for creating constructions on map.
 * Creates action for bringing materials to construction site.
 * //TODO combine with BuildingAction into ItemConsumingAction
 *
 * @author Alexander on 12.03.2019.
 */
public class ConstructionAction extends Action {
    private byte blockType;
    private List<ItemSelector> itemSelectors;

    public ConstructionAction(BuildingDesignation designation, Collection<ItemSelector> itemSelectors) {
        super(new PositionActionTarget(designation.getPosition(), BlockTypesEnum.getType(designation.getBuilding()).PASSING != 0, true));
        this.itemSelectors = new ArrayList<>(itemSelectors);
        blockType = BlockTypesEnum.getType(designation.getBuilding()).CODE;
        actionTarget.setAction(this);
    }

    /**
     * Checks that all material selectors have corresponding items in building position or in performer's inventory.
     * Creates action for bringing missing items.
     */
    @Override
    public boolean check() {
        Logger.TASKS.log("Checking " + toString());
        ArrayList<Item> uncheckedItems = new ArrayList<>(GameMvc.instance().getModel().get(ItemContainer.class).getItemsInPosition(actionTarget.getPosition())); //TODO check positions near target.
        uncheckedItems.addAll(task.getPerformer().getAspect(EquipmentAspect.class).getHauledItems()); // from performer inventory
        for (ItemSelector itemSelector : itemSelectors) {
            List<Item> selectedItems = itemSelector.selectItems(uncheckedItems);
            if (selectedItems.isEmpty()) return tryCreateDroppingAction(itemSelector); // create action for item
            for (Item selectedItem : selectedItems) {
                uncheckedItems.remove(selectedItem);
            }
        }
        return true; // all selectors have items.
    }

    @Override
    public void performLogic() {
        logStart();
        build();
    }

    private void build() {
        int material = spendItems();
        GameMvc.instance().getModel().get(LocalMap.class).setBlock(actionTarget.getPosition(), blockType, material);
    }

    private int spendItems() {
        ItemContainer itemContainer = GameMvc.instance().getModel().get(ItemContainer.class);
        List<Item> items = itemContainer.getItemsInPosition(actionTarget.getPosition());
        int mainMaterial = -1; // first item of first selector will give material.
        for (ItemSelector itemSelector : itemSelectors) {
            List<Item> itemList = itemSelector.selectItems(items);
            if (mainMaterial < 0) mainMaterial = itemList.get(0).getMaterial(); //actual material of item
            itemContainer.removeItems(itemList);                                //spend items
        }
        return mainMaterial;
    }

    /**
     * Creates action for putting item to position of this action and adds it to task.
     *
     * @param itemSelector selector for item
     * @return false if no items available.
     */
    private boolean tryCreateDroppingAction(ItemSelector itemSelector) {
        Position position = actionTarget.getPosition();
        ItemContainer itemContainer = GameMvc.instance().getModel().get(ItemContainer.class);
        if (!itemContainer.hasItemsAvailableBySelector(itemSelector, position)) return false;
        Item item = itemContainer.getItemAvailableBySelector(itemSelector, position);
        if (item == null) return false;
        ItemPutAction itemPutAction = new ItemPutAction(item, position);
        task.addFirstPreAction(itemPutAction);
        return true;
    }

    private void logStart() {
        Logger.TASKS.logDebug("Construction of " + BlockTypesEnum.getType(blockType).NAME
                + " started at " + actionTarget.getPosition()
                + " by " + task.getPerformer().toString());
    }

    @Override
    public String toString() {
        return "Construction action: " + BlockTypesEnum.getType(blockType).NAME;
    }
}
