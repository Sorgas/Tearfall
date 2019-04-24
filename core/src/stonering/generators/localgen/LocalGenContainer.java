package stonering.generators.localgen;

import stonering.entity.local.plants.PlantBlock;
import stonering.entity.local.plants.SubstratePlant;
import stonering.entity.world.World;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.entity.local.building.Building;
import stonering.entity.local.items.Item;
import stonering.entity.local.plants.AbstractPlant;
import stonering.entity.local.unit.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Stores intermediate results of local generation.
 *
 * @author Alexander Kuzyakov on 29.08.2017.
 */
public class LocalGenContainer {
    public LocalGenConfig config;
    public World world;
    public LocalMap localMap;

    public int localElevation;
    public int[][] roundedHeightsMap;
    public float[][] heightsMap;
    public float[] monthlyTemperatures;

    public ArrayList<AbstractPlant> plants;
    public List<SubstratePlant> substratePlants;
    public Map<Position, List<PlantBlock>> plantBlocks;
    public ArrayList<Unit> units;
    public ArrayList<Building> buildings;
    public ArrayList<Item> items;
    public ArrayList<Position> waterSources;
    public ArrayList<Position> waterTiles;

    public LocalGenContainer(LocalGenConfig config, World world) {
        this.config = config;
        this.world = world;
    }

    /**
     * Creates LocalMap and collections for generators.
     */
    public void initContainer() {
        localElevation = (int) (world.getWorldMap().getElevation(config.getLocation().getX(), config.getLocation().getY()) * config.getWorldToLocalElevationModifier());
        localMap = new LocalMap(config.getAreaSize(), config.getAreaSize(), localElevation + config.getAirLayersAboveGround());
        units = new ArrayList<>();
        buildings = new ArrayList<>();
        items = new ArrayList<>();
        plants = new ArrayList<>();
        waterTiles = new ArrayList<>();
        waterSources = new ArrayList<>();
        monthlyTemperatures = new float[12];
    }
}