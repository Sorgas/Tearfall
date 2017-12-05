package stonering.generators.worldgen.generators.drainage;

import stonering.generators.worldgen.generators.AbstractGenerator;
import stonering.generators.worldgen.WorldGenContainer;

import java.util.Random;

/**
 * Created by Alexander on 31.03.2017.
 *
 * Generates rainfall level in the world
 */
public class RainfallGenerator extends AbstractGenerator {
    private Random random;
    private int width;
    private int height;
    private int seaLevel;
    private float[][] rainfallBuffer;
    private int[][] rainfallComplete;

    public RainfallGenerator(WorldGenContainer container) {
        super(container);
    }

    @Override
    public boolean execute() {
        extractContainer();
        int qwe = height / 2;
        int qwer = 100 / qwe;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (container.getElevation(x, y) < seaLevel) {
                    container.setRainfall(x, y, -Math.abs(y - qwe) * qwer + 100);
                    rainfallComplete[x][y] = 1;
                }
            }
        }
        boolean haveChanges = true;
        while (haveChanges) {
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
        }
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
        return count != 0 ? rainfall / count : 0;
    }

    private void flushBufferToMap() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if(rainfallBuffer[x][y] != 0) {
                    container.setRainfall(x,y, rainfallBuffer[x][y]);
                    rainfallBuffer[x][y] = 0;
                    rainfallComplete[x][y] = 1;
                }
            }
        }
    }

    private void extractContainer() {
        random = container.getConfig().getRandom();
        width = container.getConfig().getWidth();
        height = container.getConfig().getHeight();
        seaLevel = container.getConfig().getSeaLevel();
        rainfallBuffer = new float[width][height];
        rainfallComplete = new int[width][height];
    }
}