package stonering.entity.job.action;

import stonering.entity.job.action.target.GenericBuildingAction;
import stonering.entity.job.designation.BuildingDesignation;
import stonering.entity.building.Building;
import stonering.entity.building.BuildingType;
import stonering.entity.item.Item;
import stonering.entity.item.selectors.ItemSelector;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.game.GameMvc;
import stonering.game.model.system.building.BuildingContainer;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

import java.util.Collection;
import java.util.List;

/**
 * Action for creating buildings on map.
 * Creates name for bringing materials to construction site.
 */
public class BuildingAction extends GenericBuildingAction {
    private BuildingType buildingType;

    public BuildingAction(BuildingDesignation designation, Collection<ItemSelector> itemSelectors) {
        super(designation, itemSelectors);
        buildingType = BuildingTypeMap.instance().getBuilding(designation.building);
    }

    @Override
    public void performLogic() {
        Logger.TASKS.logDebug("construction of " + buildingType.title
                + " started at " + actionTarget.getPosition()
                + " by " + task.performer.toString());
        Position target = actionTarget.getPosition();
        List<Item> items = selectItemsToConsume();
        BuildingContainer buildingContainer = GameMvc.instance().model().get(BuildingContainer.class);
        Building building = buildingContainer.buildingGenerator.generateBuilding(buildingType.building, target); //TODO use material
        buildingContainer.addBuilding(building);
        consumeItems(items);
    }

    @Override
    public String toString() {
        return "Building name: " + buildingType.title;
    }
}
