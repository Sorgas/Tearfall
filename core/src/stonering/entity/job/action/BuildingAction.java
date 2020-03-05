package stonering.entity.job.action;

import stonering.entity.building.BuildingOrder;
import stonering.entity.job.action.target.BuildingActionTarget;
import stonering.entity.building.Building;
import stonering.enums.OrientationEnum;
import stonering.enums.buildings.BuildingType;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.game.GameMvc;
import stonering.game.model.system.building.BuildingContainer;
import stonering.util.global.Logger;

/**
 * Action for creating buildings on map.
 * Creates name for bringing materials to construction site.
 */
public class BuildingAction extends GenericBuildingAction {
    private BuildingType buildingType;

    public BuildingAction(BuildingOrder order) {
        super(order);
        buildingType = BuildingTypeMap.instance().getBuilding(order.blueprint.building);

        onFinish = () -> {
            Logger.TASKS.logDebug(buildingType.title + " built at " + actionTarget.getPosition());
            BuildingContainer buildingContainer = GameMvc.model().get(BuildingContainer.class);
            BuildingActionTarget target = (BuildingActionTarget) actionTarget;
            Building building = buildingContainer.buildingGenerator.generateBuilding(buildingType.building, target.center, OrientationEnum.N); //TODO use material
            buildingContainer.addBuilding(building);
            consumeItems();
        };
    }

    @Override
    public String toString() {
        return "Building name: " + buildingType.title;
    }
}
