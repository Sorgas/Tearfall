package stonering.entity.job.action;

import stonering.entity.job.designation.BuildingDesignation;
import stonering.entity.job.action.target.PositionActionTarget;
import stonering.entity.local.building.Building;
import stonering.entity.local.building.BuildingType;
import stonering.entity.local.item.Item;
import stonering.entity.local.item.selectors.ItemSelector;
import stonering.entity.local.unit.aspects.equipment.EquipmentAspect;
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
 * Creates action for bringing materials to construction site.
 */
public class BuildingAction extends Action {
    private BuildingType buildingType;
    private List<ItemSelector> itemSelectors;

    public BuildingAction(BuildingDesignation designation, Collection<ItemSelector> itemSelectors) {
        super(new PositionActionTarget(designation.getPosition(), true, true));
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
        Logger.TASKS.log("Performing building action: " + buildingType.building);
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
        Building building = buildingContainer.getBuildingGenerator().generateBuilding(buildingType.building, target);
        buildingContainer.addBuilding(building);
    }

    /**
     * Checks that all material selectors have corresponding item in building position or in performer's inventory.
     * Creates action for bringing missing item.
     */
    @Override
    public boolean check() {
        Logger.TASKS.log("Checking building action: " + buildingType.building);
        ArrayList<Item> uncheckedItems = new ArrayList<>(GameMvc.instance().getModel().get(ItemContainer.class).getItemsInPosition(actionTarget.getPosition()));
        uncheckedItems.addAll(task.getPerformer().getAspect(EquipmentAspect.class).getHauledItems()); // from performer inventory
        for (ItemSelector itemSelector : itemSelectors) {
            List<Item> selectedItems = itemSelector.selectItems(uncheckedItems);
            if (selectedItems.isEmpty()) return tryCreateDroppingAction(itemSelector); // create action for item
            for (Item selectedItem : selectedItems) {
                uncheckedItems.remove(selectedItem);
            }
        }
        return true; // all selectors have item.
    }

    /**
     * Creates action for putting item to position of this action and adds it to task.
     *
     * @param itemSelector selector for item
     * @return false if no item available.
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
        Logger.TASKS.logDebug("construction of " + buildingType.title
                + " started at " + actionTarget.getPosition()
                + " by " + task.getPerformer().toString());
    }

    @Override
    public String toString() {
        return "Building action: " + buildingType.title;
    }
}
