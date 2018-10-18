package stonering.generators.worldgen.generators.elevation;

import stonering.generators.PerlinNoiseGenerator;
import stonering.generators.worldgen.WorldGenContainer;
import stonering.entity.world.WorldMap;
import stonering.generators.worldgen.generators.AbstractGenerator;


/**
 * @author Alexander Kuzyakov on 18.04.2018.
 */
public class ElevationModifier extends AbstractGenerator {
    private WorldMap map;
    private int width;
    private int height;
    private float[][] elevation;

    public ElevationModifier(WorldGenContainer container) {
        super(container);
        width = container.getConfig().getWidth();
        height = container.getConfig().getHeight();
        elevation = new float[width][height];
    }

    @Override
    public boolean execute() {
        generateAddition(7, 0.5f, 0.03f, 0.05f);
        applyAddition();
        return false;
    }

    private void generateAddition(int octaves, float roughness, float scale, float amplitude) {
        PerlinNoiseGenerator noise = new PerlinNoiseGenerator();
        float[][] noiseArray = noise.generateOctavedSimplexNoise(width, height, octaves, roughness, scale);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                elevation[x][y] = container.getElevation(x, y) + noiseArray[x][y] * amplitude;
            }
        }
    }

    private void applyAddition() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                container.setElevation(x, y, elevation[x][y]);
            }
        }
    }
}
