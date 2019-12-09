package stonering.generators.worldgen.generators.drainage;

import stonering.entity.world.WorldMap;
import stonering.generators.worldgen.generators.AbstractGenerator;
import stonering.generators.worldgen.WorldGenContainer;

import java.util.Random;

/**
 * @author Alexander Kuzyakov on 12.03.2017.
 */
public class OceanFiller extends AbstractGenerator {
	private Random random;
	private int width;
	private int height;
	private float seaLevel;

	public OceanFiller(WorldGenContainer container) {
		super(container);
		this.random = container.random;
		this.width = container.config.getWidth();
		this.height = container.config.getHeight();
		seaLevel = container.config.getSeaLevel();
	}

	public boolean execute() {
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
		container.setLandPart(1.0f - oceanCount / (width * height));
		return false;
	}
}
