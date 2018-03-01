package stonering.generators.localgen;

import stonering.enums.materials.MaterialMap;
import stonering.game.core.model.LocalMap;
import stonering.generators.worldgen.WorldMap;
import stonering.objects.local_actors.building.Building;
import stonering.objects.local_actors.items.Item;
import stonering.objects.local_actors.unit.Unit;
import stonering.objects.local_actors.plants.Tree;

import java.util.ArrayList;

/**
 * Created by Alexander on 29.08.2017.
 */
public class LocalGenContainer {
    private LocalGenConfig config;
    private WorldMap worldMap;
    private LocalMap localMap;
    private int[][] heightsMap;
    private MaterialMap materialMap;
    private ArrayList<Tree> trees;
    private ArrayList<Unit> units;
    private ArrayList<Building> buildings;
    private ArrayList<Item> items;

    public LocalGenContainer(LocalGenConfig config, WorldMap worldMap) {
        this.config = config;
        this.worldMap = worldMap;
        trees = new ArrayList<>();
        units = new ArrayList<>();
        buildings = new ArrayList<>();
        items = new ArrayList<>();
        materialMap = MaterialMap.getInstance();
        localMap = new LocalMap(config.getAreaSize(), config.getAreaSize(), config.getAreaHight());
    }

    public LocalGenConfig getConfig() {
        return config;
    }

    public WorldMap getWorldMap() {
        return worldMap;
    }

    public int[][] getHeightsMap() {
        return heightsMap;
    }

    public void setHeightsMap(int[][] heightsMap) {
        this.heightsMap = heightsMap;
    }

    public LocalMap getLocalMap() {
        return localMap;
    }

    public MaterialMap getMaterialMap() {
        return materialMap;
    }

    public ArrayList<Tree> getTrees() {
        return trees;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public ArrayList<Building> getBuildings() {
        return buildings;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
}