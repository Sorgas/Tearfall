package stonering.generators.worldgen.generators.elevation;

import stonering.generators.worldgen.generators.WorldGenerator;
import stonering.generators.PerlinNoiseGenerator;
import stonering.generators.worldgen.WorldGenContainer;
import stonering.util.geometry.Int2dBounds;

/**
 * Applies Perlin noise to world heightMap.
 *
 * @author Alexander Kuzyakov on 01.04.2017.
 */
public class ElevationGenerator extends WorldGenerator {
    private int width;
    private int height;
    private float[][] elevation;
    private Int2dBounds bounds;

    @Override
    public void set(WorldGenContainer container) {
        width = container.config.width;
        height = container.config.height;
        elevation = new float[width][height];
        bounds = new Int2dBounds(0, 0, width - 1, height - 1);
    }

    @Override
    public void run() {
        System.out.println("generating elevation");
        addElevation(5, 0.5f, 0.005f, 0.7f);
        addElevation(6, 0.5f, 0.015f, 0.2f);
        addElevation(7, 0.5f, 0.03f, 0.1f);
        lowerBorders();
        normalizeElevation();
        container.setElevation(0, 0, (elevation[0][1] + elevation[1][1] + elevation[1][0]) / 3f); //hack. noise generator always has 0 in (0,0)
    }

    private void addElevation(int octaves, float roughness, float scale, float amplitude) {
        PerlinNoiseGenerator noise = new PerlinNoiseGenerator();
        float[][] noiseArray = noise.generateOctavedSimplexNoise(width, height, octaves, roughness, scale);
        bounds.iterate((x, y) -> elevation[x][y] += noiseArray[x][y] * amplitude);
    }

    /**
     * Decreases elevation in a circle near borders, to create ocean on map sides
     */
    private void lowerBorders() {
        float mapRadius = (float) (width * Math.sqrt(2) / 2f);
        bounds.iterate((x, y) -> {
            float distance = Math.min(1, -10f / 4 * ((getAbsoluteDistanceToCenter(x, y) - mapRadius) / mapRadius));
            elevation[x][y] = ((elevation[x][y] + 1) * (distance)) - 1f;
        });
    }

    /**
     * Counts distance from map center to given point
     */
    private float getAbsoluteDistanceToCenter(int x, int y) {
        float dx = x - width / 2f;
        float dy = y - height / 2f;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Modifies elevation map to be within [0,1]
     */
    private void normalizeElevation() {
        bounds.iterate((x, y) -> container.setElevation(x, y, elevation[x][y] = (elevation[x][y] + 1) / 2f));
    }
}
