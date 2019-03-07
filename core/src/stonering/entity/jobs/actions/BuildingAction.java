package stonering.entity.jobs.actions;

import stonering.designations.BuildingDesignation;
import stonering.entity.jobs.actions.target.PositionActionTarget;
import stonering.entity.local.building.Building;
import stonering.entity.local.building.BuildingType;
import stonering.entity.local.items.Item;
import stonering.entity.local.items.selectors.ItemSelector;
import stonering.entity.local.unit.aspects.equipment.EquipmentAspect;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.game.core.model.lists.BuildingContainer;
import stonering.game.core.model.lists.ItemContainer;
import stonering.game.core.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.util.global.TagLoggersEnum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Action for creating buildings and constructions on map.
 * Creates actions for bringing materials to construction site.
 */
public class BuildingAction extends Action {
    private BuildingType buildingType;
    private Collection<ItemSelector> materials;

    public BuildingAction(BuildingDesignation designation, Collection<ItemSelector> materials) {
        super(null);
        this.materials = materials;
        buildingType = BuildingTypeMap.getInstance().getBuilding(designation.getBuilding());
        actionTarget = new PositionActionTarget(designation.getPosition(), !"wall".equals(buildingType.getTitle()), true);
    }

    @Override
    public void performLogic() {
        logStart();
        build();
    }

    private void build() {
        ItemContainer itemContainer = gameMvc.getModel().get(ItemContainer.class);
        Position target = actionTarget.getPosition();
        ArrayList<Item> items = itemContainer.getItems(target);
        int mainMaterial = -1; // first item of first selector will give material.
        for (ItemSelector itemSelector : materials) {
            List<Item> itemList = itemSelector.selectItems(items);
            if (mainMaterial < 0) mainMaterial = itemList.get(0).getMaterial();
            itemContainer.removeItems(itemList);
        }
        if (resolveConstructuionBlockType() > 0) {
            gameMvc.getModel().get(LocalMap.class).setBlock(target, (byte) resolveConstructuionBlockType(), mainMaterial);
        } else {
            BuildingContainer buildingContainer = gameMvc.getModel().get(BuildingContainer.class);
            Building building = buildingContainer.getBuildingGenerator().generateBuilding(buildingType.getTitle(), target);
            buildingContainer.addBuilding(building);
        }
    }

    /**
     * Returns block type code by construction name.
     *
     * @return
     */
    private int resolveConstructuionBlockType() {
        if (BlockTypesEnum.hasType(buildingType.getTitle()))
            return BlockTypesEnum.getType(buildingType.getTitle()).CODE;
        TagLoggersEnum.BUILDING.logWarn("Attempt to build construction with unknown type.");
        return -1;
    }

    /**
     * Checks that all material selectors have corresponding items in building position or in performers inventory.
     * Creates actions for bringing missing items.
     */
    @Override
    public boolean check() {
        if (resolveConstructuionBlockType() < 0) return false;
        ArrayList<Item> uncheckedItems = new ArrayList<>(gameMvc.getModel().get(ItemContainer.class).getItems(actionTarget.getPosition()));
        uncheckedItems.addAll(((EquipmentAspect) task.getPerformer().getAspects().get("equipment")).getHauledItems()); // from performer inventory
        for (ItemSelector itemSelector : materials) {
            List<Item> selectedItems = itemSelector.selectItems(uncheckedItems);
            if (selectedItems.isEmpty()) return tryCreateDroppingAction(itemSelector); // create actions for item
            for (Item selectedItem : selectedItems) {
                uncheckedItems.remove(selectedItem);
            }
        }
        return true; // all selectors have items.
    }

    /**
     * Creates action for putting item to position of this action and adds it to task.
     *
     * @param itemSelector selector for item
     * @return false if no items available.
     */
    private boolean tryCreateDroppingAction(ItemSelector itemSelector) {
        Position position = actionTarget.getPosition();
        ItemContainer itemContainer = gameMvc.getModel().get(ItemContainer.class);
        if (!itemContainer.hasItemsAvailableBySelector(itemSelector, position)) return false;
        Item item = itemContainer.getItemAvailableBySelector(itemSelector, position);
        if (item == null) return false;
        ItemPutAction itemPutAction = new ItemPutAction(item, position);
        task.addFirstPreAction(itemPutAction);
        return true;
    }

    private void logStart() {
        TagLoggersEnum.TASKS.logDebug("construction of " + buildingType.getTitle()
                + " started at " + actionTarget.getPosition()
                + " by " + task.getPerformer().toString());
    }

    @Override
    public String toString() {
        return "Building action: " + buildingType.getTitle();
    }
}
