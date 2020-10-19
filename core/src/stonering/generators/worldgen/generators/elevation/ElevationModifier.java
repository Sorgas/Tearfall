package stonering.generators.worldgen.generators.elevation;

import stonering.generators.PerlinNoiseGenerator;
import stonering.generators.worldgen.WorldGenContainer;
import stonering.entity.world.WorldMap;
import stonering.generators.worldgen.generators.WorldGenerator;
import stonering.util.geometry.Int2dBounds;


/**
 * @author Alexander Kuzyakov on 18.04.2018.
 */
public class ElevationModifier extends WorldGenerator {
    private int width;
    private int height;
    private Int2dBounds bounds;

    @Override
    public void set(WorldGenContainer container) {
        width = container.config.width;
        height = container.config.height;
        bounds = new Int2dBounds(width, height);
    }

    @Override
    public void run() {
        generateAddition(7, 0.5f, 0.03f, 0.05f);
    }

    private void generateAddition(int octaves, float roughness, float scale, float amplitude) {
        PerlinNoiseGenerator noise = new PerlinNoiseGenerator();
        float[][] noiseArray = noise.generateOctavedSimplexNoise(width, height, octaves, roughness, scale);
        bounds.iterate((x, y) -> container.elevation[x][y] += noiseArray[x][y] * amplitude);
    }
}
