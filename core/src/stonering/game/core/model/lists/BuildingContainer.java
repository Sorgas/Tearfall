package stonering.game.core.model.lists;

import stonering.game.core.model.GameContainer;
import stonering.game.core.model.LocalMap;
import stonering.generators.buildings.BuildingGenerator;
import stonering.util.geometry.Position;
import stonering.entity.local.building.Building;

import java.util.ArrayList;

/**
 * Contains all Buildings on localMap
 *
 * @author Alexander Kuzyakov on 09.12.2017.
 */
public class BuildingContainer {
    private GameContainer gameContainer;
    private ArrayList<Building> buildings;
    private LocalMap localMap;
    private BuildingGenerator buildingGenerator;

    public BuildingContainer(ArrayList<Building> buildings, GameContainer gameContainer) {
        this.buildings = buildings;
        this.gameContainer = gameContainer;
        buildingGenerator = new BuildingGenerator();
    }

    public void init() {
        buildingGenerator.init();
        buildings.forEach(building -> building.init(gameContainer));
    }

    /**
     * Places building block on local map.
     *
     * @param building
     */
    private void placeBuilding(Building building) {
        Position pos = building.getPosition();
        localMap.setBuildingBlock(pos.getX(), pos.getY(), pos.getZ(), building.getBlock());
    }

    public void placeBuildings() {
        buildings.forEach((building) -> placeBuilding(building));
    }

    public ArrayList<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(ArrayList<Building> buildings) {
        this.buildings = buildings;
    }

    public void setLocalMap(LocalMap localMap) {
        this.localMap = localMap;
    }

    public void turn() {
        for (Building building : buildings) {
            building.turn();
        }
    }

    public void addBuilding(Building building) {
        placeBuilding(building);
    }

    public BuildingGenerator getBuildingGenerator() {
        return buildingGenerator;
    }
}
