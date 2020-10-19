package stonering.generators.worldgen.generators.drainage;

import stonering.entity.world.WorldMap;
import stonering.generators.worldgen.generators.WorldGenerator;
import stonering.generators.worldgen.WorldGenContainer;

import java.util.Random;

/**
 * @author Alexander Kuzyakov on 12.03.2017.
 */
public class OceanFiller extends WorldGenerator {
	private Random random;
	private int width;
	private int height;
	private float seaLevel;

	@Override
	public void set(WorldGenContainer container) {
		this.random = container.random;
		this.width = container.config.width;
		this.height = container.config.height;
		seaLevel = container.config.seaLevel;
	}

	@Override
	public void run() {
		System.out.println("filling oceans");
		WorldMap map = container.getMap();
		float oceanCount = 0;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (map.getElevation(x,y) < seaLevel) {
					oceanCount++;
				}
			}
		}
	}
}
