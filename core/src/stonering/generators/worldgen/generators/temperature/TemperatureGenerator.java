package stonering.generators.worldgen.generators.temperature;

import stonering.generators.PerlinNoiseGenerator;
import stonering.generators.worldgen.WorldGenConfig;
import stonering.generators.worldgen.WorldGenContainer;
import stonering.generators.worldgen.generators.WorldGenerator;

/**
 * Generates temperature map of year middle temperature for world.
 *
 * @author Alexander Kuzyakov on 26.03.2017.
 */
public class TemperatureGenerator extends WorldGenerator {
    private int width;
    private int height;
    private float seaLevel;
    private float polarLineWidth;   //temperature in it is always minimal
    private float equatorLineWidth; //temperature in it is always maximum
    private float maxYearTemperature;   // max summer temperature
    private float minYearTemperature;   // min winter temperature
    private float maxSummerTemperature;
    private float minWinterTemperature;
    private float[][] yearTemperature;
    private float elevationInfluence;
    private float seasonalDeviation;     // summer and winter temperature differs from year by this.

    @Override
    public void set(WorldGenContainer container) {
        width = config.width;
        height = config.height;
        seaLevel = config.seaLevel;
        polarLineWidth = config.polarLineWidth;
        equatorLineWidth = config.equatorLineWidth;
        maxSummerTemperature = config.maxTemperature;
        minWinterTemperature = config.minTemperature;
        seasonalDeviation = config.seasonalDeviation;
        maxYearTemperature = maxSummerTemperature - seasonalDeviation;
        minYearTemperature = minWinterTemperature + seasonalDeviation;
        yearTemperature = new float[width][height];
        elevationInfluence = config.elevationInfluence;
    }

    @Override
    public void run() {
        createGradient();
        addNoiseAndElevation();
        ensureBounds();
        renderTemperature();
    }

    /**
     * Creates gradient of temperature. Gradient is mirrored on equator.
     */
    private void createGradient() {
        int height2 = height / 2;
        float polarBorder = Math.round(height * polarLineWidth);                 // y of polar border
        float equatorBorder = Math.round(height * (0.5f - equatorLineWidth));    // y of equator border
        for (int y = 0; y < height2; y++) {
            float temp = maxYearTemperature;
            if (y < polarBorder) {
                temp = minYearTemperature;
            } else if (y < equatorBorder) {
                temp = minYearTemperature + ((y - polarBorder) / (equatorBorder - polarBorder)) * (maxYearTemperature - minYearTemperature);
            }
            for (int x = 0; x < width; x++) {
                yearTemperature[x][y] = temp;
                yearTemperature[x][height - y - 1] = temp;
            }
        }
    }

    /**
     * Adds noise to temperature map and lowers temperature on high places.
     */
    private void addNoiseAndElevation() {
        //TODO add coastal and continental climates difference
        PerlinNoiseGenerator noiseGen = new PerlinNoiseGenerator();
        float[][] noise = noiseGen.generateOctavedSimplexNoise(width, height, 7, 0.6f, 0.006f);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float elevation = container.getElevation(x, y) > 0 ? container.getElevation(x, y) : 0; // sea depth counts as 0 elevation. max elevation is 1
                yearTemperature[x][y] = yearTemperature[x][y] + (noise[x][y] * 4) - (40f * elevationDelta(elevation - seaLevel));
            }
        }
    }

    /**
     * Counts summer and winter temperature, and saves in to container.
     */
    private void renderTemperature() {
        float max = 0;
        float min = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                container.setSummerTemperature(x, y, yearTemperature[x][y] + seasonalDeviation);
                container.setWinterTemperature(x, y, yearTemperature[x][y] - seasonalDeviation);
                max = Math.max(yearTemperature[x][y], max);
                min = Math.min(yearTemperature[x][y], min);
            }
        }
        System.out.println("max temp = " + max + " min temp = " + min);
    }

    /**
     * Guarantees that temperature will be within bounds.
     */
    private void ensureBounds() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float rainfall = yearTemperature[x][y];
                yearTemperature[x][y] = rainfall > maxYearTemperature ? maxYearTemperature : (rainfall < minYearTemperature ? minYearTemperature : rainfall);
            }
        }
    }

    /**
     * Counts temperature decreasing rate for elevation.
     * @param elevation
     * @return
     */
    private float elevationDelta(float elevation) {
        float value = (float) (Math.pow(2, elevation * elevationInfluence) / (elevationInfluence * elevationInfluence));
//        System.out.println(elevation + " " + value);
        return value;
    }
}