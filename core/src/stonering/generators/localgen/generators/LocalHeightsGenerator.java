package stonering.generators.localgen.generators;

import stonering.generators.localgen.LocalGenConfig;
import stonering.generators.localgen.LocalGenContainer;
import stonering.generators.worldgen.WorldMap;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Alexander on 21.08.2017.
 */
public class LocalHeightsGenerator {
    private LocalGenContainer container;
    private WorldMap worldMap;
    private LocalGenConfig config;
    private int localAreaSize;
    private float[][] localHightMap;

    public LocalHeightsGenerator(LocalGenContainer container) {
        this.container = container;
        this.worldMap = container.getWorldMap();
        config = container.getConfig();
        localAreaSize = config.getAreaSize();
    }

    public void execute() {
        int x = config.getLocation().getX();
        int y = config.getLocation().getY();
        localHightMap = new float[localAreaSize + 1][localAreaSize + 1];
        for (int i = 0; i < localAreaSize + 1; i++) {
            Arrays.fill(localHightMap[i], -1);
        }
        calculateCorners(localHightMap, x, y);
        calculateBorders(localHightMap, x, y);
        diamondSquare(localHightMap);
        fillHeights(localHightMap, 6);
        container.setHeightsMap(roundLocalHightMap());
    }

    private void calculateCorners(float[][] localHights, int x, int y) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                localHights[i * localAreaSize][j * localAreaSize] = calculateMidElevationForCorner(x + i, y + j);
            }
        }
    }

    private void calculateBorders(float[][] localHights, int x, int y) {
        float[] border = new float[localAreaSize + 1];

        //west border
        for (int i = 0; i < localAreaSize + 1; i++) {
            border[i] = localHights[0][i];
        }
        recursiveMidpoint(border, 0, localAreaSize, new Random(x * y + 1));

        //east border
        for (int i = 0; i < localAreaSize + 1; i++) {
            localHights[0][i] = border[i];
            border[i] = localHights[localAreaSize][i];
        }
        recursiveMidpoint(border, 0, localAreaSize, new Random((x + 1) * y + 1));

        //south border
        for (int i = 0; i < localAreaSize + 1; i++) {
            localHights[localAreaSize][i] = border[i];
            border[i] = localHights[i][0];
        }
        recursiveMidpoint(border, 0, localAreaSize, new Random(x * y));

        //north border
        for (int i = 0; i < localAreaSize + 1; i++) {
            localHights[i][0] = border[i];
            border[i] = localHights[i][localAreaSize];
        }
        recursiveMidpoint(border, 0, localAreaSize, new Random(x * (y + 1)));
        for (int i = 0; i < localAreaSize + 1; i++) {
            localHights[i][localAreaSize] = border[i];
        }
    }

    private void recursiveMidpoint(float[] array, int left, int right, Random random) {
        if (right - left > 6) {
            array[(right + left) / 2] = (array[left] + array[right]) / 2 + random.nextInt(2) - 1;
            recursiveMidpoint(array, left, (right + left) / 2, random);
            recursiveMidpoint(array, (right + left) / 2, right, random);
//            System.out.println(left + "   " + right + " | " + array[(right + left) / 2]);
        } else {
            for (int i = left + 1; i < right; i++) {
                array[i] = array[left] + ((array[left] - array[right]) / (right - left)) * i;
            }
        }
    }

    private void diamondSquare(float[][] localHights) {
        int step = localAreaSize;
        while (step > 6) {
            performSquare(localHights, step);
            performDiamond(localHights, step);
            step /= 2;
        }
    }

    private void performSquare(float[][] array, int step) {
        for (int x = 0; x + step < localAreaSize + 1; x += step) {
            for (int y = 0; y + step < localAreaSize + 1; y += step) {
                float midValue = array[x][y];
                midValue += array[x + step][y];
                midValue += array[x][y + step];
                midValue += array[x + step][y + step];
                array[x + step / 2][y + step / 2] = (int) (midValue / 4);
//                System.out.println("s: " + (x + step / 2) + " " + (y + step / 2) + " " + " " + array[x + step / 2][y + step / 2]);
            }
        }
    }

    private void performDiamond(float[][] array, int step) {
        for (int x = 0; x < localAreaSize + 1; x += step) {
            for (int y = 0; y < localAreaSize + 1; y += step) {
                if (x + step / 2 < localAreaSize + 1 && array[x + step / 2][y] < 0)
                    calculateDiamond(array, step, x + step / 2, y);
                if (y + step / 2 < localAreaSize + 1 && array[x][y + step / 2] < 0)
                    calculateDiamond(array, step, x, y + step / 2);
            }
        }
    }

    private void calculateDiamond(float[][] array, int step, int x, int y) {
        int arraySize = localAreaSize + 1;
        array[x][y] = 0;
        int count = 0;
        if (x + step / 2 < arraySize) {
            array[x][y] += array[x + step / 2][y];
            count++;
        }
        if (x - step / 2 >= 0) {
            array[x][y] += array[x - step / 2][y];
            count++;
        }
        if (y + step / 2 < arraySize) {
            array[x][y] += array[x][y + step / 2];
            count++;
        }
        if (y - step / 2 >= 0) {
            array[x][y] += array[x][y - step / 2];
            count++;
        }
        array[x][y] /= count;
    }

    private void fillHeights(float[][] array, int step) {
        for (int x = 0; x < localAreaSize; x += step) {
            for (int y = 0; y < localAreaSize; y += step) {
                fillSquare(array, x, y, step);
            }
        }
    }

    private void fillSquare(float[][] array, int x, int y, int step) {
        for (int i = 0; i <= step; i++) {
            for (int j = 0; j <= step; j++) {
                array[x + i][y + j] = getMidValue(array[x][y], array[x + step][y],
                        array[x][y + step], array[x + step][y + step], i, j);
            }
        }
    }

    private float getMidValue(float sw, float se, float nw, float ne, int x, int y) {
        float n = nw + (ne - nw) * x / 6f;
        float s = sw + (se - sw) * x / 6f;
        return s + (n - s) * y / 6f;
    }

    //counts elevation for SW corner of world cell
    private int calculateMidElevationForCorner(int x, int y) {
        int elevation = 0;
        int count = 0;
        for (int i = x - 1; i <= x; i++) {
            for (int j = y - 1; j <= y; j++) {
                if (i >= 0 && j >= 0) {
                    elevation += worldMap.getElevation(i, j);
                    count++;
                }
            }
        }
        elevation /=count;
        elevation *=config.getWorldToLocalElevationModifier();
        return elevation + config.getLocalSeaLevel();
    }

    private int[][] roundLocalHightMap() {
        int[][] result = new int[localHightMap.length][localHightMap.length];
        for (int x = 0; x < localHightMap.length; x++) {
            for (int y = 0; y < localHightMap.length; y++) {
                result[x][y] = Math.round(localHightMap[x][y]);
            }
        }
        return result;
    }
}