package stonering.menu.worldgen.generators.world.generators.elevation;

import stonering.menu.worldgen.generators.world.WorldGenContainer;
import stonering.menu.worldgen.generators.world.WorldMap;

import java.util.Random;

/**
 * Created by Alexander on 24.04.2017.
 */
public class DiamondSquareGenerator {
	private WorldGenContainer container;
	private WorldMap map;
	private WorldMap map2;
	private Random random;
	private int density = 8;
	private int numX;
	private int numY;

	public DiamondSquareGenerator(WorldGenContainer container) {
		this.container = container;
	}

	public void execute() {
		init();
		while (density > 1) {
			performSquare();
			performDiamond();
			density /= 2;
			numX *= 2;
			numY *= 2;
		}
		System.out.println(density);
		container.setMap2(map2);
	}

	private void init() {
		map = container.getMap();
		random = container.getConfig().getRandom();
		map2 = new WorldMap((map.getWidth() - 1) * density + 1, (map.getHeight() - 1) * density + 1);
		for (int x = 0; x < map.getWidth(); x++) {
			for (int y = 0; y < map.getHeight(); y++) {
				map2.getCell(x * density, y * density).setElevation(map.getCell(x, y).getElevation());
			}
		}
		numX = map.getWidth() - 1;
		numY = map.getHeight() - 1;
		System.out.println(map2.getWidth());
	}

	private void performSquare() {
		for (int x = 0; x < numX; x++) {
			for (int y = 0; y < numY; y++) {
				float midValue = map2.getCell(x * density, y * density).getElevation();
				midValue += map2.getCell((x + 1) * density, y * density).getElevation();
				midValue += map2.getCell(x * density, (y + 1) * density).getElevation();
				midValue += map2.getCell((x + 1) * density, (y + 1) * density).getElevation();
				map2.getCell(x * density + density / 2, y * density + density / 2).setElevation(midValue / 4);
			}
		}
	}

	private void performDiamond() {
		for (int x = 0; x < numX + 1; x++) {
			for (int y = 0; y < numY + 1; y++) {
				if (x < numX) {
					float midValue = map2.getCell(x * density, y * density).getElevation();
					midValue += map2.getCell((x + 1) * density, y * density).getElevation();
					int count = 2;
					if (y > 0) {
						midValue += map2.getCell(x * density + density / 2, y * density - density / 2).getElevation();
						count++;
					}
					if (y < numY - 1) {
						midValue += map2.getCell(x * density + density / 2, y * density + density / 2).getElevation();
						count++;
					}
					map2.getCell(x * density + density / 2, y * density).setElevation(midValue / count);
				}
				if (y < numY) {
					float midValue = map2.getCell(x * density, y * density).getElevation();
					midValue += map2.getCell(x * density, (y + 1) * density).getElevation();
					int count = 2;
					if (x > 0) {
						midValue += map2.getCell(x * density - density / 2, y * density + density / 2).getElevation();
						count++;
					}
					if (x < numX - 1) {
						midValue += map2.getCell(x * density + density / 2, y * density + density / 2).getElevation();
						count++;
					}
					map2.getCell(x * density, y * density + density / 2).setElevation(midValue / count);
				}
			}
		}
	}

	public void setContainer(WorldGenContainer container) {
		this.container = container;
	}
}
