package stonering.generators.localgen;

import stonering.game.core.model.LocalMap;
import stonering.generators.worldgen.WorldMap;
import stonering.objects.local_actors.building.Building;
import stonering.objects.local_actors.items.Item;
import stonering.objects.local_actors.plants.Plant;
import stonering.objects.local_actors.unit.Unit;

import java.util.ArrayList;

/**
 * Created by Alexander on 29.08.2017.
 * <p>
 * Stores intermediate results of local generation.
 */
public class LocalGenContainer {
    private LocalGenConfig config;
    private WorldMap worldMap;
    private LocalMap localMap;

    private int localElevation;
    private int[][] heightsMap;
    private float[] monthlyTemperatures;

    private ArrayList<Plant> plants;
    private ArrayList<Unit> units;
    private ArrayList<Building> buildings;
    private ArrayList<Item> items;

    public LocalGenContainer(LocalGenConfig config, WorldMap worldMap) {
        this.config = config;
        this.worldMap = worldMap;
    }

    /**
     * Creates LocalMap and collections for generators.
     */
    public void initContainer() {
        localElevation = (int) (worldMap.getElevation(config.getLocation().getX(), config.getLocation().getY())
                * config.getWorldToLocalElevationModifier());
        localMap = new LocalMap(config.getAreaSize(), config.getAreaSize(),
                localElevation + config.getAirLayersAboveGround());
        units = new ArrayList<>();
        buildings = new ArrayList<>();
        items = new ArrayList<>();
        plants = new ArrayList<>();
        monthlyTemperatures = new float[12];
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

    public ArrayList<Plant> getPlants() {
        return plants;
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

    public float[] getMonthlyTemperatures() {
        return monthlyTemperatures;
    }

    public int getLocalElevation() {
        return localElevation;
    }
}