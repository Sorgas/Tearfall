package stonering.generators.worldgen.generators.drainage;

import stonering.generators.PerlinNoiseGenerator;
import stonering.generators.worldgen.generators.AbstractGenerator;
import stonering.generators.worldgen.WorldGenContainer;

/**
 * Created by Alexander on 31.03.2017.
 * <p>
 * Generates rainfall level in the world
 */
public class RainfallGenerator extends AbstractGenerator {
    private int width;
    private int height;
    private int seaLevel;
    private int minRainfall;
    private int maxRainfall;
    private float[][] rainfallBuffer;
    private int[][] rainfallComplete;

    public RainfallGenerator(WorldGenContainer container) {
        super(container);
    }

    @Override
    public boolean execute() {
        System.out.println("generating rainfall");
        extractContainer();
        float equator = height / 2f;
        for (int y = 0; y < height; y++) {
            float rainfall = ((-Math.abs(y - (equator))) / (equator) + 1) * (maxRainfall - minRainfall) + minRainfall;
            for (int x = 0; x < width; x++) {
                if (container.getElevation(x, y) < seaLevel) {
                    container.setRainfall(x, y, rainfall);
                    rainfallComplete[x][y] = 1;
                }
            }
        }
        boolean haveChanges = false;
        do {
            haveChanges = false;
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (rainfallComplete[x][y] == 0 && hasNearRainfall(x, y)) {
                        haveChanges = true;
                        rainfallBuffer[x][y] = countNearRainfall(x, y);
                    }
                }
            }
            flushBufferToMap();
        } while (haveChanges);
        addPerlinNoise();
        return false;
    }

    private boolean hasNearRainfall(int x, int y) {
        int xStart = x > 0 ? x - 1 : 0;
        int xEnd = x < width - 1 ? x + 1 : width - 1;
        int yStart = y > 0 ? y - 1 : 0;
        int yEnd = y < height - 1 ? y + 1 : height - 1;
        for (x = xStart; x <= xEnd; x++) {
            for (y = yStart; y <= yEnd; y++) {
                if (rainfallComplete[x][y] != 0) return true;
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
                if (rainfallComplete[x][y] != 0) {
                    rainfall += container.getRainfall(x, y);
                    count++;
                }
            }
        }
        rainfall = count != 0 ? rainfall / count : 0;
        rainfall = rainfall * (200 - container.getElevation(x, y)) / 200f;
        return rainfall;
    }

    private void flushBufferToMap() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (rainfallBuffer[x][y] != 0) {
                    container.setRainfall(x, y, rainfallBuffer[x][y]);
                    rainfallBuffer[x][y] = 0;
                    rainfallComplete[x][y] = 1;
                }
            }
        }
    }

    private void addPerlinNoise() {
        PerlinNoiseGenerator generator = new PerlinNoiseGenerator();
        float[][] noise = generator.generateOctavedSimplexNoise(width, height, 7, 0.4f, 0.025f);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                container.setRainfall(x, y, Math.round(container.getRainfall(x, y) + noise[x][y] * 10));
            }
        }
    }

    private void extractContainer() {
        width = container.getConfig().getWidth();
        height = container.getConfig().getHeight();
        seaLevel = container.getConfig().getSeaLevel();
        minRainfall = container.getConfig().getMinRainfall();
        maxRainfall = container.getConfig().getMaxRainfall();
        rainfallBuffer = new float[width][height];
        rainfallComplete = new int[width][height];
    }
}