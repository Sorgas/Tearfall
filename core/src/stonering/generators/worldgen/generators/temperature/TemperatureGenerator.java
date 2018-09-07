package stonering.generators.worldgen.generators.temperature;

import stonering.generators.PerlinNoiseGenerator;
import stonering.generators.worldgen.WorldGenConfig;
import stonering.generators.worldgen.WorldGenContainer;
import stonering.generators.worldgen.generators.AbstractGenerator;

/**
 * Generates temperature map of year middle temperature for world.
 *
 * @author Alexander on 26.03.2017.
 */
public class TemperatureGenerator extends AbstractGenerator {
    private int width;
    private int height;
    private float polarLineWidth; //temperature in it is always minimal
    private float equatorLineWidth; //temperature in it is always maximum
    private float maxTemperature;
    private float minTemperature;
    private float[][] temperature;
    private float elevationInfluence;

    public TemperatureGenerator(WorldGenContainer container) {
        super(container);
    }

    private void extractContainer(WorldGenContainer container) {
        WorldGenConfig config = container.getConfig();
        width = config.getWidth();
        height = config.getHeight();
        polarLineWidth = config.getPolarLineWidth();
        equatorLineWidth = config.getEquatorLineWidth();
        maxTemperature = config.getMaxTemperature();
        minTemperature = config.getMinTemperature();
        temperature = new float[width][height];
        elevationInfluence = config.getElevationInfluence();
    }

    @Override
    public boolean execute() {
        extractContainer(container);
        createGradient();
        renderTemperature();
        return false;
    }

    private void createGradient() {
        int height2 = height / 2;
        float polarBorder = Math.round(height * polarLineWidth);
        float equatorBorder = Math.round(height * (0.5f - equatorLineWidth));
        for (int y = 0; y < height2; y++) {
            float temp = maxTemperature;
            if (y < polarBorder) {
                temp = minTemperature;
            } else if (y < equatorBorder) {
                temp = minTemperature + ((y - polarBorder) / (equatorBorder - polarBorder)) * (maxTemperature - minTemperature);
            }
            for (int x = 0; x < width; x++) {
                temperature[x][y] = temp;
                temperature[x][height - y - 1] = temp;
            }
        }
    }

    private void renderTemperature() {
        PerlinNoiseGenerator noiseGen = new PerlinNoiseGenerator();
        float[][] noise = noiseGen.generateOctavedSimplexNoise(width, height, 7, 0.6f, 0.006f);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float elevation = container.getElevation(x, y) > 0 ? container.getElevation(x, y) : 0; // sea depth counts as 0 elevation.
                //TODO add coastal and continental climates difference
                float baseTemperature = temperature[x][y] + noise[x][y] * 4 - elevation * elevationInfluence;
                container.setSummerTemperature(x, y, baseTemperature + 5);
                container.setWinterTemperature(x, y, baseTemperature - 5);
            }
        }
    }
}