package stonering.generators.buildings;

import stonering.entity.local.building.BuildingType;
import stonering.entity.local.building.aspects.WorkbenchAspect;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.global.utils.Position;
import stonering.entity.local.building.Building;

/**
 * @author Alexander Kuzyakov on 07.12.2017.
 * <p>
 * Generates BuildingType entity from descriptors
 */
public class BuildingGenerator {
    private BuildingTypeMap buildingTypeMap;

    public BuildingGenerator() {
        init();
    }

    public void init() {
        buildingTypeMap = BuildingTypeMap.getInstance();
    }

    public Building generateBuilding(String name, Position position) {
        BuildingType type = BuildingTypeMap.getInstance().getBuilding(name);
        Building building = new Building(position, type);
        building.setName(type.getTitle());
        building.setMaterial(38); //TODO replace with material from task
        addAspects(building, type);
        return building;
    }

    private void addAspects(Building building, BuildingType type) {
        if (type.getCategory().equals("workbenches")) {
            WorkbenchAspect workbenchAspect = new WorkbenchAspect(building);
            building.getAspects().put("workbench", workbenchAspect);
        }
    }
}
