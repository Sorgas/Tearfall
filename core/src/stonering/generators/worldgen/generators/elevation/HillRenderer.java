package stonering.generators.worldgen.generators.elevation;

import stonering.generators.worldgen.WorldGenContainer;
import stonering.generators.worldgen.generators.WorldGenerator;
import stonering.entity.world.Mountain;
import stonering.entity.world.Slope;
import stonering.util.geometry.Position;

import java.util.Iterator;
import java.util.List;

/**
 * @author Alexander Kuzyakov on 12.03.2017.
 */
public class HillRenderer  extends WorldGenerator {
	private int width;
	private int height;
	private float[][] elevation;
	private int smoothIterations = 1;
	private int smoothRadius = 2;

	@Override
	public void set(WorldGenContainer container) {
		this.width = container.config.width;
		this.height = container.config.height;
		elevation = new float[width][height];
	}

	@Override
	public void run() {
		System.out.println("rendering hills");
//		for (Iterator<Mountain> iterator = container.getHills().iterator(); iterator.hasNext(); ) {
//			Mountain hill = iterator.next();
//			renderHill(hill);
//		}
		smoothHills(smoothIterations);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				container.setElevation(x, y, container.getElevation(x,y) + elevation[x][y]);
			}
		}
	}

	private void renderHill(Mountain hill) {
		List<Position> corners = hill.getCorners();
		Position prevCorner = corners.get(corners.size() - 1);
		for (Iterator<Position> iterator = hill.getCorners().iterator(); iterator.hasNext(); ) {
			Position corner = iterator.next();
			Slope slope = new Slope(hill.getTop(), prevCorner, corner);
			prevCorner = corner;
			renderSlope(slope);
		}
	}

	private void renderSlope(Slope slope) {
		int minX = slope.getMinX() > 0 ? slope.getMinX() : 0;
		int minY = slope.getMinY() > 0 ? slope.getMinY() : 0;
		int maxX = slope.getMaxX() < width - 1 ? slope.getMaxX() : width - 1;
		int maxY = slope.getMaxY() < height - 1 ? slope.getMaxY() : height - 1;
		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				if (slope.isInside(x, y)) {
					elevation[x][y] = slope.getAltitude(x, y);
				}
			}
		}
	}

	private void smoothHills(int iterations) {
		float[][] innerElevationBuffer = new float[width][height];
		for (int i = 0; i < iterations; i++) {
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					innerElevationBuffer[x][y] = countMiddleElevation(x, y, smoothRadius, true);
				}
			}
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					elevation[x][y] = innerElevationBuffer[x][y];
				}
			}
		}
	}

	private float countMiddleElevation(int x, int y, int radius, boolean lockBorders) {
		if ((x - radius < 0) || (x + radius >= width) || (y - radius < 0) || (y + radius >= height)) {
			return elevation[x][y];
		}
		float sum = 0;
		for (int i = x - radius; i < x + radius; i++) {
			for (int j = y - radius; j < y + radius; j++) {
				sum += elevation[i][j];
			}
		}
		return sum / (float) (Math.pow(2 * radius + 1, 2));
	}
}