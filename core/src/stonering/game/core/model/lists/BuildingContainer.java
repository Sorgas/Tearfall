package stonering.game.core.model.lists;

import stonering.game.core.model.LocalMap;
import stonering.global.utils.Position;
import stonering.objects.local_actors.building.Building;

import java.util.ArrayList;

/**
 * Created by Alexander on 09.12.2017.
 *
 * Contains all Buildings on localMap
 */
public class BuildingContainer {
    private ArrayList<Building> buildings;
    private LocalMap localMap;

    public BuildingContainer(ArrayList<Building> buildings) {
        this.buildings = buildings;
    }

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
    }

    public void addBuilding(Building building) {
        placeBuilding(building);
    }
}
