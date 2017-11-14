package stonering.generators.localgen.generators;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.core.model.LocalMap;
import stonering.generators.PerlinNoiseGenerator;
import stonering.generators.localgen.LocalGenConfig;
import stonering.generators.localgen.LocalGenContainer;
import stonering.generators.worldgen.WorldMap;
import stonering.global.utils.Position;

import java.util.Random;

/**
 * Created by Alexander on 22.10.2017.
 */
public class LocalCaveGenerator {
    private LocalGenContainer container;
    private WorldMap worldMap;
    private LocalGenConfig config;
    private int localAreaSize;
    private LocalMap localMap;
    private Random random;

    public LocalCaveGenerator(LocalGenContainer container) {
        this.container = container;
        worldMap = container.getWorldMap();
        config = container.getConfig();
        localAreaSize = config.getAreaSize();
        localMap = container.getLocalMap();
        random = new Random();
    }

    public void execute() {
        int localElevation = worldMap.getElevation(config.getLocation().getX(), config.getLocation().getY()) * config.getWorldToLocalElevationModifier() + config.getLocalSeaLevel();
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
            int x = random.nextInt(config.getAreaSize() - 2) + 1;
            int y = random.nextInt(config.getAreaSize() - 2) + 1;
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
        for (int x = 0; x < config.getAreaSize(); x++) {
            for (int y = 0; y < config.getAreaSize(); y++) {
                for (int z = height - (layer[x][y] / 10); z < layer[x][y] + height; z++) {
                    localMap.setBlock(x, y, z, BlockTypesEnum.SPACE, 0);
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
            int x = random.nextInt(config.getAreaSize() - 10) + 5;
            int y = random.nextInt(config.getAreaSize() - 10) + 5;
            if (localMap.getBlockType(x, y, top) == BlockTypesEnum.SPACE.getCode()) continue;
            if (localMap.getBlockType(x, y, bottom) == BlockTypesEnum.SPACE.getCode()) continue;

            boolean reject = false;
            for (int i = xs.length - 1; i > count - 1; i--) {
                Position pos = new Position(x, y, 0);
                if (pos.getDistanse(xs[i], ys[i], 0) < 15) {
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
                        localMap.setBlock(x, y, z, BlockTypesEnum.SPACE, 0);
                    }
                }
            }
        }
    }
}
