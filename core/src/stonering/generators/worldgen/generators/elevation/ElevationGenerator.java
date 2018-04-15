package stonering.generators.worldgen.generators.elevation;

import stonering.generators.worldgen.generators.AbstractGenerator;
import stonering.generators.PerlinNoiseGenerator;
import stonering.generators.worldgen.WorldGenContainer;

/**
 * Created by Alexander on 01.04.2017.
 * <p>
 * applies Perlin noise to heightMap
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
        addElevation(5, 0.5f, 0.005f, 0.7f);
        addElevation(6, 0.5f, 0.015f, 0.2f);
        addElevation(7, 0.5f, 0.03f, 0.1f);
        lowerBorders();
        normalizeElevation();
        //hack. noise generator always has 0 in (0,0)
        container.setElevation(0, 0, (elevation[0][1] + elevation[1][1] + elevation[1][0]) / 3f);
        return false;
    }

    /**
     * adds Perlin noise with parameters and amplitude to elevation array
     *
     * @param octaves
     * @param roughness
     * @param scale
     * @param amplitude
     */
    private void addElevation(int octaves, float roughness, float scale, float amplitude) {
        PerlinNoiseGenerator noise = new PerlinNoiseGenerator();
        float[][] noiseArray = noise.generateOctavedSimplexNoise(width, height, octaves, roughness, scale);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                elevation[x][y] += noiseArray[x][y] * amplitude;
            }
        }
    }

    /**
     * decreases elevation in a circle near borders, to create ocean on map sides
     */
    private void lowerBorders() {
        float mapRadius = (float) (width * Math.sqrt(2) / 2f);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                //
                float distance = Math.min(1, - 10f / 4 *((getAbsoluteDistanceToCenter(x, y) - mapRadius)/ mapRadius));
                elevation[x][y] = ((elevation[x][y] + 1) * (distance)) - 1f;
            }
        }
    }

    /**
     * counts distance from map center to given point
     * @param x x
     * @param y y
     * @return distance
     */
    private float getAbsoluteDistanceToCenter(int x, int y) {
        float dx = x - width / 2f;
        float dy = y - height / 2f;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * modifies elevation map to be within [0,1]
     */
    private void normalizeElevation() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                elevation[x][y] = (elevation[x][y] + 1) / 2f;
                container.setElevation(x, y, elevation[x][y]);
            }
        }
    }

}
