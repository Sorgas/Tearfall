package stonering.menu.worldgen;

import stonering.TearFall;
import stonering.generators.worldgen.*;
import stonering.menu.mvc_interfaces.GameModel;

import java.util.Random;

/**
 * Created by Alexander on 06.03.2017.
 */
public class WorldGenModel implements GameModel {
    private WorldGenContainer worldGenContainer;
    private WorldGeneratorContainer worldGeneratorContainer;
    private WorldMap map;
    private long seed; // gets updated from ui
    private int worldSize = 100; // changed from ui

    private WorldGenView view;
    private TearFall game;


    private Random random;

    public WorldGenModel(TearFall game) {
        this.game = game;
        random = new Random();
        seed = random.nextLong();
        init();
    }

    public void generateWorld() { //from ui button
        WorldGenConfig config = new WorldGenConfig(seed, worldSize, worldSize);
        worldGeneratorContainer = new WorldGeneratorContainer();
        worldGeneratorContainer.init(config);
        worldGeneratorContainer.runContainer();
        map = worldGeneratorContainer.getWorldMap();
    }

    @Override
    public void init() {

    }

    public void setView(WorldGenView view) {
        this.view = view;
    }

    public WorldMap getMap() {
        return map;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public long getSeed() {
        return seed;
    }

    public int getWorldSize() {
        return worldSize;
    }

    public void setWorldSize(int worldSize) {
        this.worldSize = worldSize;
    }
}