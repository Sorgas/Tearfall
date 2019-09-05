package stonering.entity.job.action;

import stonering.entity.job.designation.BuildingDesignation;
import stonering.entity.job.action.target.PositionActionTarget;
import stonering.entity.item.Item;
import stonering.entity.item.selectors.ItemSelector;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.ItemContainer;
import stonering.game.model.system.PlantContainer;
import stonering.game.model.system.SubstrateContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static stonering.entity.job.action.target.ActionTarget.ANY;
import static stonering.entity.job.action.target.ActionTarget.NEAR;
import static stonering.enums.blocks.BlockTypesEnum.NOT_PASSABLE;

/**
 * Action for creating constructions on map.
 * Creates name for bringing materials to construction site.
 * //TODO combine with BuildingAction into ItemConsumingAction
 *
 * @author Alexander on 12.03.2019.
 */
public class ConstructionAction extends Action {
    private byte blockType;
    private List<ItemSelector> itemSelectors;

    public ConstructionAction(BuildingDesignation designation, Collection<ItemSelector> itemSelectors) {
        super(new PositionActionTarget(designation.getPosition(), BlockTypesEnum.getType(designation.getBuilding()).PASSING == NOT_PASSABLE ? NEAR : ANY));
        this.itemSelectors = new ArrayList<>(itemSelectors);
        blockType = BlockTypesEnum.getType(designation.getBuilding()).CODE;
        actionTarget.setAction(this);
    }

    /**
     * Checks that all material selectors have corresponding item in building position or in performer's inventory.
     * Creates name for bringing missing item.
     */
    @Override
    public int check() {
        Logger.TASKS.log("Checking " + toString());
        ArrayList<Item> uncheckedItems = new ArrayList<>(GameMvc.instance().getModel().get(ItemContainer.class).getItemsInPosition(actionTarget.getPosition())); //TODO checkItems positions near target.
        uncheckedItems.addAll(task.getPerformer().getAspect(EquipmentAspect.class).getHauledItems()); // from performer inventory
        for (ItemSelector itemSelector : itemSelectors) {
            List<Item> selectedItems = itemSelector.selectItems(uncheckedItems);
            if (selectedItems.isEmpty()) return tryCreateDroppingAction(itemSelector); // create name for item
            for (Item selectedItem : selectedItems) {
                uncheckedItems.remove(selectedItem);
            }
        }
        return OK; // all selectors have item.
    }

    @Override
    public void performLogic() {
        logStart();
        build();
    }

    private void build() {
        Position target = actionTarget.getPosition();
        GameMvc.instance().getModel().get(LocalMap.class).setBlock(target, blockType, spendItems());
        PlantContainer container = GameMvc.instance().getModel().get(PlantContainer.class);
        container.remove(container.getPlantInPosition(target), true);
        SubstrateContainer substrateContainer = GameMvc.instance().getModel().get(SubstrateContainer.class);
        substrateContainer.remove(substrateContainer.getSubstrateInPosition(target));
    }

    private int spendItems() {
        ItemContainer itemContainer = GameMvc.instance().getModel().get(ItemContainer.class);
        List<Item> items = itemContainer.getItemsInPosition(actionTarget.getPosition());
        int mainMaterial = -1; // first item of first selector will give material.
        for (ItemSelector itemSelector : itemSelectors) {
            List<Item> itemList = itemSelector.selectItems(items);
            if (mainMaterial < 0) mainMaterial = itemList.get(0).getMaterial(); //actual material of item
            itemContainer.removeItems(itemList);                                //spend item
        }
        return mainMaterial;
    }

    /**
     * Creates name for putting item to position of this name and adds it to task.
     *
     * @param itemSelector selector for item
     * @return false if no item available.
     */
    private int tryCreateDroppingAction(ItemSelector itemSelector) {
        Position position = actionTarget.getPosition();
        ItemContainer itemContainer = GameMvc.instance().getModel().get(ItemContainer.class);
        if (!itemContainer.hasItemsAvailableBySelector(itemSelector, position)) return FAIL;
        Item item = itemContainer.getItemAvailableBySelector(itemSelector, position);
        if (item == null) return FAIL;
        ItemPutAction itemPutAction = new ItemPutAction(item, position);
        task.addFirstPreAction(itemPutAction);
        return NEW;
    }

    private void logStart() {
        Logger.TASKS.logDebug("Construction of " + BlockTypesEnum.getType(blockType).NAME
                + " started at " + actionTarget.getPosition()
                + " by " + task.getPerformer().toString());
    }

    @Override
    public String toString() {
        return "Construction name: " + BlockTypesEnum.getType(blockType).NAME;
    }
}
