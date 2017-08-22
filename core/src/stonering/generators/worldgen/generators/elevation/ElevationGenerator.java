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
		perlin();
		return false;
	}

	private void perlin() {
		PerlinNoiseGenerator noise = new PerlinNoiseGenerator();
		elevation = noise.generateOctavedSimplexNoise(width, height, 7, 0.5f, 0.03f);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				elevation[x][y] += 1.5f;
				container.setElevation(x, y, (float) (container.getElevation(x, y) + Math.pow(elevation[x][y], 2f)));
			}
		}
	}
}
