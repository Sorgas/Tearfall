package stonering.game.core.model.lists;

import stonering.game.core.GameMvc;
import stonering.game.core.model.ModelComponent;
import stonering.game.core.model.Turnable;
import stonering.game.core.model.local_map.LocalMap;
import stonering.generators.buildings.BuildingGenerator;
import stonering.entity.local.building.Building;
import stonering.util.global.Initable;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains all Buildings on localMap
 *
 * @author Alexander Kuzyakov on 09.12.2017.
 */
public class BuildingContainer extends Turnable implements ModelComponent, Initable {
    private List<Building> buildings;
    private LocalMap localMap;
    private BuildingGenerator buildingGenerator;

    public BuildingContainer() {
        this(new ArrayList<>());
    }

    public BuildingContainer(List<Building> buildings) {
        buildingGenerator = new BuildingGenerator();
        this.buildings = buildings;
    }

    public void init() {
        localMap = GameMvc.getInstance().getModel().get(LocalMap.class);
        buildingGenerator.init();
        buildings.forEach(this::placeBuilding);
    }

    /**
     * Places building block on local map.
     */
    private void placeBuilding(Building building) {
        building.init();
        localMap.setBuildingBlock(building.getPosition(), building.getBlock());
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public void turn() {
        for (Building building : buildings) {
            building.turn();
        }
    }

    /**
     * Adds building to container and places it on map.
     */
    public void addBuilding(Building building) {
        buildings.add(building);
        placeBuilding(building);
    }

    public BuildingGenerator getBuildingGenerator() {
        return buildingGenerator;
    }
}
