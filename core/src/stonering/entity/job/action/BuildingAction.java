package stonering.entity.job.action;

import stonering.entity.job.action.target.ActionTarget;
import stonering.entity.job.designation.BuildingDesignation;
import stonering.entity.job.action.target.PositionActionTarget;
import stonering.entity.building.Building;
import stonering.entity.building.BuildingType;
import stonering.entity.item.Item;
import stonering.entity.item.selectors.ItemSelector;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.game.GameMvc;
import stonering.game.model.lists.BuildingContainer;
import stonering.game.model.lists.ItemContainer;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Action for creating buildings on map.
 * Creates name for bringing materials to construction site.
 */
public class BuildingAction extends Action {
    private BuildingType buildingType;
    private List<ItemSelector> itemSelectors;

    public BuildingAction(BuildingDesignation designation, Collection<ItemSelector> itemSelectors) {
        super(new PositionActionTarget(designation.getPosition(), ActionTarget.ANY));
        this.itemSelectors = new ArrayList<>(itemSelectors);
        buildingType = BuildingTypeMap.getInstance().getBuilding(designation.getBuilding());
        actionTarget.setAction(this);
    }

    @Override
    public void performLogic() {
        logStart();
        build();
    }

    private void build() {
        Logger.TASKS.log("Performing building name: " + buildingType.building);
        ItemContainer itemContainer = GameMvc.instance().getModel().get(ItemContainer.class);
        Position target = actionTarget.getPosition();
        List<Item> items = itemContainer.getItemsInPosition(target);
        int mainMaterial = -1; // first item of first selector will give material.
        for (ItemSelector itemSelector : itemSelectors) {
            List<Item> itemList = itemSelector.selectItems(items);
            if (mainMaterial < 0) mainMaterial = itemList.get(0).getMaterial();
            itemContainer.removeItems(itemList);
        }
        BuildingContainer buildingContainer = GameMvc.instance().getModel().get(BuildingContainer.class);
        Building building = buildingContainer.buildingGenerator.generateBuilding(buildingType.building, target);
        buildingContainer.addBuilding(building);
    }

    /**
     * Checks that all material selectors have corresponding item in building position or in performer's inventory.
     * Creates name for bringing missing item.
     */
    @Override
    public int check() {
        Logger.TASKS.log("Checking building name: " + buildingType.building);
        ArrayList<Item> uncheckedItems = new ArrayList<>(GameMvc.instance().getModel().get(ItemContainer.class).getItemsInPosition(actionTarget.getPosition()));
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

    /**
     * Creates name for putting item to position of this name and adds it to task.
     *
     * @param itemSelector selector for item
     * @return FAIL if no item available.
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
        Logger.TASKS.logDebug("construction of " + buildingType.title
                + " started at " + actionTarget.getPosition()
                + " by " + task.getPerformer().toString());
    }

    @Override
    public String toString() {
        return "Building name: " + buildingType.title;
    }
}
