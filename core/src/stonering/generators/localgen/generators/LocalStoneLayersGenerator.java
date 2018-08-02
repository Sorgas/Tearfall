package stonering.generators.localgen.generators;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.materials.MaterialMap;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.game.core.model.LocalMap;
import stonering.generators.localgen.LocalGenContainer;

import java.util.*;

/**
 * Fills localmap with blocks according to heightsMap.
 * Values in heights map are z of highest wall block.
 *
 * @author Alexander Kuzyakov on 01.08.2017.
 */
public class LocalStoneLayersGenerator {
    private LocalGenContainer container;
    private LocalMap map;
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

    public LocalStoneLayersGenerator(LocalGenContainer container) {
        this.container = container;

    }

    public void execute() {
        System.out.println("generating stone layers");
        map = this.container.getLocalMap();
        heigtsMap = this.container.getRoundedHeightsMap();
        surfaceLevel = container.getLocalElevation();
        layerIds = new int[surfaceLevel];
        if (surfaceLevel > 300) {
            hasExtrusive = true;
        }countLayers();
        try {
            generateLayers();
        } catch (DescriptionNotFoundException e) {
            e.printStackTrace();
        }
        fillLayers();
    }

    private void fillLayers() {
        int id;
        for (int x = 0; x < map.getxSize(); x++) {
            for (int y = 0; y < map.getySize(); y++) {
                for (int z = map.getzSize() - 1; z >= 0; z--) {
                    if (z <= heigtsMap[x][y]) { //non space sell
                        id = z - (heigtsMap[x][y] - (layerIds.length - 1));
                        id = id < 0 ? 0 : id;
                        map.setBlock(x, y, z, BlockTypesEnum.WALL, layerIds[id]);
                    }
                }
            }
        }
    }

    private void countLayers() {
        soilLayer = (int) (10 - (surfaceLevel - container.getConfig().getWorldToLocalElevationModifier() * 0.5f) / 20);

        intrusiveLayer = surfaceLevel - 150;

        metamorficLayer = surfaceLevel / 2 + 50;

        sedimentaryLayer = surfaceLevel - soilLayer;
        extrusiveLayer = -1;
        if (surfaceLevel > 300) {
            extrusiveLayer = sedimentaryLayer;
            sedimentaryLayer = 450 - surfaceLevel / 2;
        }
    }

    private void generateLayers() throws DescriptionNotFoundException {
        int i = layerIds.length - 1;
        int soilId = MaterialMap.getInstance().getId("soil");
        for (int soilIndex = 0; soilIndex < soilLayer; soilIndex++) {
            layerIds[i] = soilId;
            i--;
        }

        int[] globalLayers = {extrusiveLayer - sedimentaryLayer,
                sedimentaryLayer - metamorficLayer,
                metamorficLayer - intrusiveLayer,
                intrusiveLayer};
        int[] maxSubLayerNumber = container.getConfig().getSublayerMaxCount();
        int[] minSubLayerThickness = container.getConfig().getSublayerMinThickness();
        for (int g = 0; g < globalLayers.length; g++) {
            if (globalLayers[g] > 0) { //check thickness
                int subLayerNumber = Math.min(globalLayers[g] / minSubLayerThickness[g] + 1, maxSubLayerNumber[g]);
                int[] ids = getIdsByStoneType(subLayerNumber, 1, g);
                float globalLayerThickness = globalLayers[g];
                for (int k = 0; k < subLayerNumber; k++) {
                    int subLayerThickness = Math.round(globalLayerThickness / (float) (subLayerNumber - k));
                    globalLayerThickness -= subLayerThickness;
                    for (; subLayerThickness > 0 && i >= 0; i--) {
                        layerIds[i] = ids[k];
                        subLayerThickness--;
                    }
                }
            }
        }
    }

    public int[] getIdsByStoneType(int num, int seed, int stoneType) throws DescriptionNotFoundException {
        Random random = new Random(seed);
        MaterialMap materialMap = MaterialMap.getInstance();
        int[] ids = new int[num];
        LinkedList<String> stoneTypeNames = new LinkedList<>(Arrays.asList(stoneTypes[stoneType]));
        for (int i = 0; i < num; i++) {
            if (stoneTypeNames.size() == 0) {
                stoneTypeNames = new LinkedList<>(Arrays.asList(stoneTypes[stoneType]));
                Collections.shuffle(stoneTypeNames, random);
            }
            ids[i] = materialMap.getId(stoneTypeNames.get(0));
            stoneTypeNames.remove(0);
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