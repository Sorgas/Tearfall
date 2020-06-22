package stonering.entity.job.action;

import stonering.entity.building.BuildingOrder;
import stonering.entity.job.action.item.GenericBuildingAction;
import stonering.entity.job.action.target.BuildingActionTarget;
import stonering.entity.building.Building;
import stonering.enums.buildings.BuildingType;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.game.GameMvc;
import stonering.game.model.system.building.BuildingContainer;
import stonering.game.model.system.plant.PlantContainer;
import stonering.util.logging.Logger;

/**
 * Action for creating buildings on map.
 * Creates action for bringing materials to construction site.
 */
public class BuildingAction extends GenericBuildingAction {
    private BuildingType buildingType;

    public BuildingAction(BuildingOrder order) {
        super(order);
        buildingType = BuildingTypeMap.getBuilding(order.blueprint.building);

        onFinish = () -> {
            Logger.TASKS.logDebug(buildingType.title + " built at " + this.target.getPosition());
            BuildingContainer buildingContainer = GameMvc.model().get(BuildingContainer.class);
            BuildingActionTarget target = (BuildingActionTarget) this.target;
            Building building = buildingContainer.buildingGenerator.generateBuilding(buildingType.building, target.center, order.orientation); //TODO use material
            buildingContainer.addBuilding(building);
            PlantContainer plantContainer = GameMvc.model().get(PlantContainer.class);
            getBuildingBounds().stream().forEach(vector -> plantContainer.removeBlock(vector.x, vector.y, order.position.z, false));
            consumeItems();
        };
    }

    @Override
    public String toString() {
        return "Building name: " + buildingType.title;
    }
}
