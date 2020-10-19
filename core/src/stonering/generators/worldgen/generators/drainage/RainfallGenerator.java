package stonering.generators.worldgen.generators.drainage;

import stonering.generators.PerlinNoiseGenerator;
import stonering.generators.worldgen.generators.WorldGenerator;
import stonering.generators.worldgen.WorldGenContainer;

/**
 * Generates rainfall level in the world
 *
 * @author Alexander Kuzyakov on 31.03.2017.
 */
public class RainfallGenerator extends WorldGenerator {
    private int width;
    private int height;
    private float seaLevel;
    private int minRainfall;
    private int maxRainfall;

    private float[][] rainfallBuffer;
    private boolean[][] rainfallSet;

    @Override
    public void set(WorldGenContainer container) {
        width = container.config.width;
        height = container.config.height;
        seaLevel = container.config.seaLevel;
        minRainfall = container.config.minRainfall;
        maxRainfall = container.config.maxRainfall;
        rainfallBuffer = new float[width][height];
        rainfallSet = new boolean[width][height];
    }

    @Override
    public void run() {
        System.out.println("generating rainfall");
        addMainGradientOnWater();
        fillEmptyInBuffer();
        addPerlinNoise();
        ensureBounds();
    }

    /**
     * Creates rainfall gradient above water
     */
    private void addMainGradientOnWater() {
        float equator = height / 2f;
        for (int y = 0; y < height; y++) {
            // gradient has min on poles and max on equator.
            float rainfall = ((-Math.abs(y - (equator))) / (equator) + 1) * (maxRainfall - minRainfall) + minRainfall;
            for (int x = 0; x < width; x++) {
                if (container.getElevation(x, y) <= seaLevel) {
                    container.setRainfall(x, y, rainfall);
                    rainfallSet[x][y] = true;
                }
            }
        }
    }

    /**
     * Fills rainfall above land, starting from coasts.
     */
    private void fillEmptyInBuffer() {
        for (int i = 0; i < Math.max(height, width); i++) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (!rainfallSet[x][y] && hasNearRainfall(x, y)) {
                        rainfallBuffer[x][y] = countNearRainfall(x, y);
                        rainfallBuffer[x][y] *= 0.98f;
                    }
                }
            }
            flushBufferToMap();
        }
    }

    private boolean hasNearRainfall(int x, int y) {
        int xStart = x > 0 ? x - 1 : 0;
        int xEnd = x < width - 1 ? x + 1 : width - 1;
        int yStart = y > 0 ? y - 1 : 0;
        int yEnd = y < height - 1 ? y + 1 : height - 1;
        for (x = xStart; x <= xEnd; x++) {
            for (y = yStart; y <= yEnd; y++) {
                if (rainfallSet[x][y]) return true;
            }
        }
        return false;
    }

    private float countNearRainfall(int x, int y) {
        int xStart = x > 0 ? x - 1 : 0;
        int xEnd = x < width - 1 ? x + 1 : width - 1;
        int yStart = y > 0 ? y - 1 : 0;
        int yEnd = y < height - 1 ? y + 1 : height - 1;
        float rainfall = 0;
        int count = 0;
        for (x = xStart; x <= xEnd; x++) {
            for (y = yStart; y <= yEnd; y++) {
                if (rainfallSet[x][y]) {
                    rainfall += container.getRainfall(x, y);
                    count++;
                }
            }
        }
        rainfall = count != 0 ? rainfall / count : 0;
        rainfall *= (80 - container.getElevation(x, y)) / 80f;
        return rainfall;
    }

    private void flushBufferToMap() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (rainfallBuffer[x][y] != 0) {
                    container.setRainfall(x, y, rainfallBuffer[x][y]);
                    rainfallBuffer[x][y] = 0;
                    rainfallSet[x][y] = true;
                }
            }
        }
    }

    /**
     * Adds Perlin noise on rainfall map
     */
    private void addPerlinNoise() {
        PerlinNoiseGenerator generator = new PerlinNoiseGenerator();
        float[][] noise = generator.generateOctavedSimplexNoise(width, height, 7, 0.4f, 0.025f);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                container.setRainfall(x, y, Math.round(container.getRainfall(x, y) + noise[x][y] * 10));
            }
        }
    }

    /**
     * Guarantees that rainfall will be within bounds.
     */
    private void ensureBounds() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float rainfall = container.getRainfall(x, y);
                container.setRainfall(x, y, rainfall > maxRainfall ? maxRainfall : (rainfall < minRainfall ? minRainfall : rainfall));
            }
        }
    }
}