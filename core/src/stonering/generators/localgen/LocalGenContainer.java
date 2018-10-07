package stonering.generators.localgen;

import stonering.game.core.model.LocalMap;
import stonering.generators.worldgen.WorldMap;
import stonering.global.utils.Position;
import stonering.objects.local_actors.building.Building;
import stonering.objects.local_actors.items.Item;
import stonering.objects.local_actors.plants.AbstractPlant;
import stonering.objects.local_actors.unit.Unit;

import java.util.ArrayList;

/**
 * Stores intermediate results of local generation.
 *
 * @author Alexander Kuzyakov on 29.08.2017.
 */
public class LocalGenContainer {
    private LocalGenConfig config;
    private WorldMap worldMap;
    private LocalMap localMap;

    private int localElevation;
    private int[][] roundedHeightsMap;
    private float[][] heightsMap;
    private float[] monthlyTemperatures;

    private ArrayList<AbstractPlant> plants;
    private ArrayList<Unit> units;
    private ArrayList<Building> buildings;
    private ArrayList<Item> items;
    private ArrayList<Position> waterSources;
    private ArrayList<Position> waterTiles;

    public LocalGenContainer(LocalGenConfig config, WorldMap worldMap) {
        this.config = config;
        this.worldMap = worldMap;
    }

    /**
     * Creates LocalMap and collections for generators.
     */
    public void initContainer() {
        localElevation = (int) (worldMap.getElevation(config.getLocation().getX(), config.getLocation().getY()) * config.getWorldToLocalElevationModifier());
        localMap = new LocalMap(config.getAreaSize(), config.getAreaSize(), localElevation + config.getAirLayersAboveGround());
        units = new ArrayList<>();
        buildings = new ArrayList<>();
        items = new ArrayList<>();
        plants = new ArrayList<>();
        waterTiles = new ArrayList<>();
        waterSources = new ArrayList<>();
        monthlyTemperatures = new float[12];
    }

    public LocalGenConfig getConfig() {
        return config;
    }

    public WorldMap getWorldMap() {
        return worldMap;
    }

    public int[][] getRoundedHeightsMap() {
        return roundedHeightsMap;
    }

    public void setRoundedHeightsMap(int[][] roundedHeightsMap) {
        this.roundedHeightsMap = roundedHeightsMap;
    }

    public LocalMap getLocalMap() {
        return localMap;
    }

    public ArrayList<AbstractPlant> getPlants() {
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

    public float[][] getHeightsMap() {
        return heightsMap;
    }

    public void setHeightsMap(float[][] heightsMap) {
        this.heightsMap = heightsMap;
    }

    public ArrayList<Position> getWaterSources() {
        return waterSources;
    }

    public void setWaterSources(ArrayList<Position> waterSources) {
        this.waterSources = waterSources;
    }

    public ArrayList<Position> getWaterTiles() {
        return waterTiles;
    }

    public void setWaterTiles(ArrayList<Position> waterTiles) {
        this.waterTiles = waterTiles;
    }
}