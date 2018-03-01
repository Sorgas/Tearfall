package stonering.generators.worldgen.generators.elevation;

import stonering.generators.worldgen.generators.AbstractGenerator;
import stonering.generators.PerlinNoiseGenerator;
import stonering.generators.worldgen.WorldGenContainer;

/**
 * Created by Alexander on 01.04.2017.
 */
public class ElevationGenerator extends AbstractGenerator {
    private int width;
    private int height;
    private float[][] elevation;

    public ElevationGenerator(WorldGenContainer container) {
        super(container);
        extractContainer(container);
    }

    private void extractContainer(WorldGenContainer container) {
        width = container.getConfig().getWidth();
        height = container.getConfig().getHeight();
        elevation = new float[width][height];
    }

    @Override
    public boolean execute() {
        System.out.println("generating elevation");
        addElevation(5, 0.5f, 0.005f, 1f);
        addElevation(6, 0.5f, 0.015f, 0.5f);
        addElevation(7, 0.5f, 0.03f, 0.25f);
        float mapRadius = (float) (width * Math.sqrt(2) / 2f);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float distance = getAbsoluteDistanceToCenter(x, y) / mapRadius;
                elevation[x][y] = ((elevation[x][y] + 0.5f) * (1 - distance));
                elevation[x][y] = (float) Math.pow((elevation[x][y]) * 2, 3f);
                container.setElevation(x, y, elevation[x][y]);
            }
        }
        return false;
    }

    private void addElevation(int octaves, float roughness, float scale, float multiplier) {
        PerlinNoiseGenerator noise = new PerlinNoiseGenerator();
        float[][] noiseArray = noise.generateOctavedSimplexNoise(width, height, octaves, roughness, scale);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                elevation[x][y] += noiseArray[x][y] * multiplier;
            }
        }
    }

    private float getDistanceToBorder(int x, int y) {
        float dx = 2f * x / width - 1;
        float dy = 2f * y / height - 1;
        return 2 * (float) (Math.sqrt(dx * dx + dy * dy));
    }

    private float getAbsoluteDistanceToCenter(int x, int y) {
        float dx = x - width / 2f;
        float dy = y - height / 2f;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}
