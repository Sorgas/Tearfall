package stonering.generators.localgen;

import stonering.enums.BlockTypesEnum;
import stonering.enums.materials.MaterialMap;
import stonering.exceptions.MaterialNotFoundException;
import stonering.game.core.model.LocalMap;

import java.util.*;

/**
 * Created by Alexander on 01.08.2017.
 */
public class LocalStoneLayersGenerator {
    private LocalGenContainer container;
    private LocalMap map;
    private MaterialMap materialMap;
    private int[][] heigtsMap;
    private int surfaceLevel;
    private int soilLayer;
    private int sedimentaryLayer;
    private int metamorficLayer;
    private int intrusiveLayer;
    private int extrusiveLayer;
    private boolean hasExtrusive;
    private int[] layerIds;
    private String[][] stoneTypes = {
            {"rhyolite", "andesite", "obsidian", "tuff", "basalt"}, //extrusive
            {"shale", "limestone", "sandstone", "dolomite", "chalk", "siltstone", "chert"},  //sedimentary
            {"schist", "chlorite", "phyllite", "quarzite", "gneiss", "marble"}, //metamorfic
            {"granite", "diorite", "gabbro", "peridotite", "pegmatite"}};  //intrusive

    public LocalStoneLayersGenerator(LocalGenContainer localGenContainer) {
        container = localGenContainer;
        heigtsMap = container.getHeightsMap();
        map = container.getLocalMap();
        materialMap = container.getMaterialMap();
        surfaceLevel = container.getWorldMap().getElevation(container.getConfig().getLocation().getX(), container.getConfig().getLocation().getX());
        surfaceLevel *= container.getConfig().getWorldToLocalElevationModifier();
        surfaceLevel += 200;
        layerIds = new int[surfaceLevel];
        if (surfaceLevel > 300) {
            hasExtrusive = true;
        }
    }

    public void execute() {
        heigtsMap = container.getHeightsMap();
        countLayers();
        try {
            generateLayers();
        } catch (MaterialNotFoundException e) {
            e.printStackTrace();
        }
        fillLayers();
    }

    private void fillLayers() {
        int id = 0;
        for (int x = 0; x < map.getxSize(); x++) {
            for (int y = 0; y < map.getySize(); y++) {
                int groundLevel = -1;
                for (int z = map.getzSize() - 1; z >= 0; z--) {
                    if (z <= heigtsMap[x][y]) { //non space sell
                        id = layerIds.length - heigtsMap[x][y];
                        id = id >= 0 ? id : 1;
                        map.setBlock(x, y, z, BlockTypesEnum.WALL, layerIds[id]);
                    }
                }
            }
        }
    }

    private void countLayers() {
        soilLayer = surfaceLevel - 16 + surfaceLevel / 25;

        intrusiveLayer = surfaceLevel - 150;

        metamorficLayer = surfaceLevel / 2 + 50;

        sedimentaryLayer = surfaceLevel - 16 + surfaceLevel / 25;
        extrusiveLayer = -1;
        if (surfaceLevel > 300) {
            extrusiveLayer = sedimentaryLayer;
            sedimentaryLayer = 450 - surfaceLevel / 2;
        }
    }

    private void generateLayers() throws MaterialNotFoundException {
        int soilBottom = Math.min(sedimentaryLayer, extrusiveLayer);
        int i = layerIds.length - 1;
        for (; i > soilBottom; i--) {
            layerIds[i] = materialMap.getId("soil");
        }
        int[] layers = {extrusiveLayer - sedimentaryLayer, sedimentaryLayer - metamorficLayer,
                metamorficLayer - intrusiveLayer, intrusiveLayer};
        int[] maxSubLayerNumber = container.getConfig().getSublayerMaxCount();
        int[] minSubLayerThickness = container.getConfig().getSublayerMinThickness();
        for (int j = 0; j < layers.length; j++) { //
            if (layers[j] > 0) {
                int subLayerNumber = Math.min(layers[j] / minSubLayerThickness[j] + 1, maxSubLayerNumber[j]);
                int[] ids = getIdsByStoneType(subLayerNumber, 1, j);
                for (int k = 0; k < subLayerNumber; k++) {
                    int subLayerThickness = layers[j] / (subLayerNumber - k);
                    layers[j] -= subLayerThickness;
                    for (; subLayerThickness > 0 && i >= 0; i--) {
                        layerIds[i] = ids[k];
                        subLayerThickness--;
                    }
                }
            }
        }
    }

    public int[] getIdsByStoneType(int num, int seed, int stoneType) throws MaterialNotFoundException {
        Random random = new Random(seed);
        int[] ids = new int[num];
        LinkedList<String> names = new LinkedList<>(Arrays.asList(stoneTypes[stoneType]));
        for (int i = 0; i < num; i++) {
            int nameNum = random.nextInt(names.size() - 1);
            ids[i] = materialMap.getId(names.get(nameNum));
            names.remove(nameNum);
        }
        return ids;
    }

    public void setSurfaceLevel(int surfaceLevel) {
        this.surfaceLevel = surfaceLevel;
    }

    public void setHeigtsMap(int[][] heigtsMap) {
        this.heigtsMap = heigtsMap;
    }
}