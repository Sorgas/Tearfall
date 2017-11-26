package stonering.generators.localgen;

import stonering.enums.materials.MaterialMap;
import stonering.game.core.model.LocalMap;
import stonering.generators.worldgen.WorldMap;
import stonering.objects.plants.Tree;

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

    public LocalGenContainer(LocalGenConfig config, WorldMap worldMap) {
        this.config = config;
        this.worldMap = worldMap;
        trees = new ArrayList<>();
        materialMap = new MaterialMap();
        createMap();
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

    private void createMap() {
        localMap = new LocalMap(config.getAreaSize(),config.getAreaSize(), config.getAreaHight());
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
}