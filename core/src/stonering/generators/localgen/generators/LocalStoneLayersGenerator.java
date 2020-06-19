package stonering.generators.localgen.generators;

import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.materials.MaterialMap;
import stonering.game.model.local_map.LocalMap;
import stonering.generators.localgen.LocalGenContainer;
import stonering.util.logging.Logger;

import java.util.*;

/**
 * Fills localmap with blocks according to heightsMap.
 * Values in heights map are z of highest wall block.
 * //TODO reduce number of stonetypes.
 *
 * @author Alexander Kuzyakov on 01.08.2017.
 */
public class LocalStoneLayersGenerator extends LocalGenerator {
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
            {"rhyolite", "obsidian", "basalt"}, //extrusive
            {"limestone", "sandstone", "dolomite", "shale"}, //sedimentary
            {"quarzite", "gneiss", "marble"}, //metamorfic
            {"granite", "diorite", "gabbro"}}; //intrusive

    public LocalStoneLayersGenerator(LocalGenContainer container) {
        super(container);
    }

    public void execute() {
        Logger.GENERATION.logDebug("generating stone layers");
        map = this.container.model.get(LocalMap.class);
        heigtsMap = this.container.roundedHeightsMap;
        surfaceLevel = container.localElevation;
        layerIds = new int[surfaceLevel];
        hasExtrusive = surfaceLevel > 300;
        countLayers();
        generateLayers();
        fillLayers();
    }

    private void fillLayers() {
        int id;
        for (int x = 0; x < map.xSize; x++) {
            for (int y = 0; y < map.ySize; y++) {
                for (int z = map.zSize - 1; z >= 0; z--) {
                    if (z <= heigtsMap[x][y]) { //non space sell
                        id = z - (heigtsMap[x][y] - (layerIds.length - 1));
                        id = id < 0 ? 0 : id;
                        map.blockType.setBlock(x, y, z, BlockTypeEnum.WALL.CODE, layerIds[id]);
                    }
                }
            }
        }
    }

    private void countLayers() {
        soilLayer = (int) (10 - (surfaceLevel - container.config.getWorldToLocalElevationModifier() * 0.5f) / 20);

        intrusiveLayer = surfaceLevel - 150;

        metamorficLayer = surfaceLevel / 2 + 50;

        sedimentaryLayer = surfaceLevel - soilLayer;
        extrusiveLayer = -1;
        if (surfaceLevel > 300) {
            extrusiveLayer = sedimentaryLayer;
            sedimentaryLayer = 450 - surfaceLevel / 2;
        }
    }

    private void generateLayers() {
        int i = layerIds.length - 1;
        int soilId = MaterialMap.instance().getId("soil");
        for (int soilIndex = 0; soilIndex < soilLayer; soilIndex++) {
            layerIds[i] = soilId;
            i--;
        }

        int[] globalLayers = {extrusiveLayer - sedimentaryLayer,
                sedimentaryLayer - metamorficLayer,
                metamorficLayer - intrusiveLayer,
                intrusiveLayer};
        int[] maxSubLayerNumber = container.config.getSublayerMaxCount();
        int[] minSubLayerThickness = container.config.getSublayerMinThickness();
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

    public int[] getIdsByStoneType(int num, int seed, int stoneType) {
        Random random = new Random(seed);
        MaterialMap materialMap = MaterialMap.instance();
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
}