package stonering.generators.localgen.generators;

import stonering.enums.blocks.BlockTypeEnum;
import stonering.game.model.local_map.LocalMap;
import stonering.generators.PerlinNoiseGenerator;
import stonering.generators.localgen.LocalGenContainer;
import stonering.util.geometry.Position;
import stonering.util.logging.Logger;

import java.util.Random;

/**
 * @author Alexander Kuzyakov on 22.10.2017.
 */
public class LocalCaveGenerator extends LocalGenerator {
    private int localAreaSize;
    private LocalMap localMap;
    private Random random;

    public LocalCaveGenerator(LocalGenContainer container) {
        super(container);
        localAreaSize = config.areaSize;
        random = new Random();
    }

    public void execute() {
        Logger.GENERATION.logDebug("generating caves");
        localMap = container.model.get(LocalMap.class);
        int localElevation = localMap.zSize - config.getAirLayersAboveGround();
        int step = 50;
        int prevLeyer = -1;
        for (int z = localElevation - step; z > 20; z -= step) {
            appllyLayer(generateLayer(), z);
            if (prevLeyer > 0) {
                placeMines(prevLeyer, z, 5);
            }
            prevLeyer = z;
        }
    }

    private int[][] generateLayer() {
        int caveLayerHeight = random.nextInt(config.getMaxCaveLayerHeight() - config.getMinCaveLayerHeight()) + config.getMinCaveLayerHeight();
        PerlinNoiseGenerator noise = new PerlinNoiseGenerator();
        float[][] noiseArray = noise.generateOctavedSimplexNoise(localAreaSize, localAreaSize, 7, 0.5f, 0.03f);
        int[][] caveHeightMap = new int[noiseArray.length][noiseArray[0].length];
        for (int i = 0; i < noiseArray.length; i++) {
            for (int j = 0; j < noiseArray[0].length; j++) {
                noiseArray[i][j] += 0.5f;
            }
        }
        placePillars(noiseArray, 100);
        for (int i = 0; i < noiseArray.length; i++) {
            for (int j = 0; j < noiseArray[0].length; j++) {
                caveHeightMap[i][j] = Math.round(noiseArray[i][j] * caveLayerHeight);
            }
        }
        return caveHeightMap;
    }

    private void placePillars(float[][] noiseArray, int num) {
        random = new Random();
        while (num > 0) {
            int x = random.nextInt(config.areaSize
                    - 2) + 1;
            int y = random.nextInt(config.areaSize
                    - 2) + 1;
            if (noiseArray[x][y] > 0.4f) {
                noiseArray[x - 1][y] = noiseArray[x][y] * ((random.nextInt(7) + 7) * 0.05f);
                noiseArray[x + 1][y] = noiseArray[x][y] * ((random.nextInt(7) + 7) * 0.05f);
                noiseArray[x][y - 1] = noiseArray[x][y] * ((random.nextInt(7) + 7) * 0.05f);
                noiseArray[x][y + 1] = noiseArray[x][y] * ((random.nextInt(7) + 7) * 0.05f);
                noiseArray[x][y] = 0;
                num--;
            }
        }
    }

    private void appllyLayer(int[][] layer, int height) {
        for (int x = 0; x < config.areaSize
                ; x++) {
            for (int y = 0; y < config.areaSize
                    ; y++) {
                for (int z = height - (layer[x][y] / 10); z < layer[x][y] + height; z++) {
                    localMap.blockType.setBlock(x, y, z, BlockTypeEnum.SPACE.CODE, 0);
                }
            }
        }
    }

    private void placeMines(int top, int bottom, int count) {
        Random random = new Random();
        int[] xs = new int[count];
        int[] ys = new int[count];
        int rejects = 20;
        while (count > 0 && rejects > 0) {
            int x = random.nextInt(config.areaSize
                    - 10) + 5;
            int y = random.nextInt(config.areaSize
                    - 10) + 5;
            if (localMap.blockType.get(x, y, top) == BlockTypeEnum.SPACE.CODE) continue;
            if (localMap.blockType.get(x, y, bottom) == BlockTypeEnum.SPACE.CODE) continue;

            boolean reject = false;
            for (int i = xs.length - 1; i > count - 1; i--) {
                Position pos = new Position(x, y, 0);
                if (pos.getDistance(xs[i], ys[i], 0) < 15) {
                    reject = true;
                    break;
                }
            }

            if (!reject) {
                cutMine(top, bottom, x, y, 4);
                xs[count - 1] = x;
                ys[count - 1] = y;
                count--;
            } else {
                rejects--;
            }
        }
    }

    private void cutMine(int top, int bottom, int xc, int yc, int r) {
        for (int x = xc - r; x <= xc + r; x++) {
            for (int y = yc - r; y <= yc + r; y++) {
                if (Math.pow((float) (x - xc), 2) + Math.pow((float) (y - yc), 2) < r * r) {
                    for (int z = bottom; z <= top; z++) {
                        localMap.blockType.setBlock(x, y, z, BlockTypeEnum.SPACE.CODE, 0);
                    }
                }
            }
        }
    }
}
