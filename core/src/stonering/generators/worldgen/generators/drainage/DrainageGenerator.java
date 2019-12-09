package stonering.generators.worldgen.generators.drainage;

import stonering.generators.worldgen.WorldGenConfig;
import stonering.generators.worldgen.WorldGenContainer;
import stonering.generators.worldgen.generators.AbstractGenerator;

/**
 * @author Alexander Kuzyakov
 */
public class DrainageGenerator extends AbstractGenerator {
    private float[][] evaporation;
    private float[][] slopesDrainage;
    private float[][] basinsDrainage;
    private int width;
    private int height;

    public DrainageGenerator(WorldGenContainer container) {
        super(container);
    }

    @Override
    public boolean execute() {
        extractContainer();
        createEvaporationMap();
        countSlopeDrainage();
        applyBasins();
        renderDrainage();
        return false;
    }

    private void extractContainer() {
        width = container.config.getWidth();
        height = container.config.getHeight();
        evaporation = new float[width][height];
        slopesDrainage = new float[width][height];
        basinsDrainage = new float[width][height];
    }

    /**
     * Steep areas have higher drainage than flat ones.
     */
    private void countSlopeDrainage() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                slopesDrainage[x][y] = countSlope(x, y);
            }
        }
    }

    /**
     * Water evaporates faster from warm places. Colder regions tend to accumulate water.
     * High rainfall lowers evaporation.
     */
    private void createEvaporationMap() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float temperature = (container.getSummerTemperature(x, y) + container.getWinterTemperature(x, y)) / 2f;
                if (temperature > -10) {
                    evaporation[x][y] = temperature / container.config.getMaxTemperature();
                    evaporation[x][y] *= 1-  container.getRainfall(x, y) / container.config.getMaxRainfall();
                }
            }
        }
    }

    private void renderDrainage() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (container.getElevation(x, y) > container.config.getSeaLevel())
                    container.setDrainage(x, y, evaporation[x][y] + slopesDrainage[x][y] + basinsDrainage[x][y]);
            }
        }
    }

    /**
     * Rivers & brooks increase drainage in humid areas and lower it in dry areas.
     * There should not be swamps around rivers in common, and deserts should turnUnit to savannas.
     */
    private void applyBasins() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float temperature = (container.getSummerTemperature(x, y) + container.getWinterTemperature(x, y)) / 2f;
                if(temperature > 0) {
                    if(container.getRiver(x,y) != null) {
                        applyDrainageAround(x,y);
                    }
                    if(container.getBrook(x,y) != null) {
                        //
                    }
                }
            }
        }
    }

    private void applyDrainageAround(int x, int y) {
        basinsDrainage[x][y] = -0.1f;
    }

    /**
     * Counts medium elevation delta for near lower cells;
     *
     * @return
     */
    private float countSlope(int x, int y) {
        float delta = 0;
        for (int dx = -1; dx < 2; dx++) {
            for (int dy = -1; dy < 2; dy++) {
                if (container.getMap().inMap(x + dx, y + dy)) {
                    delta = Math.max(delta, container.getElevation(x, y) - container.getElevation(x + dx, y + dy));
                }
            }
        }
        return delta * 2;
    }
}
