package stonering.generators.worldgen.generators.elevation;

import stonering.generators.worldgen.WorldGenContainer;
import stonering.generators.worldgen.generators.WorldGenerator;
import stonering.entity.world.Edge;
import stonering.entity.world.Mountain;
import stonering.entity.world.Slope;
import stonering.util.geometry.Position;

import java.util.Iterator;
import java.util.List;

/**
 * @author Alexander Kuzyakov on 10.03.2017.
 */
public class ValleyRenderer  extends WorldGenerator {
	private int width;
	private int height;
	private float[][] elevation;
	private int smoothIterations = 0;
	private int smoothRadius = 1;

	@Override
	public void set(WorldGenContainer container) {
		this.width = container.config.width;
		this.height = container.config.height;
		this.smoothIterations = container.config.smoothIterations;
		this.smoothRadius = container.config.smoothRadius;
		elevation = new float[width][height];
	}

	@Override
	public void run() {
		System.out.println("rendering valleys");
//		for (Iterator<Edge> edgeIterator = container.getEdges().iterator(); edgeIterator.hasNext(); ) {
//			for (Iterator<Mountain> iterator = edgeIterator.next().getValleys().iterator(); iterator.hasNext(); ) {
//				Mountain valley = iterator.next();
//				renderValley(valley);
//			}
//		}
		smoothValleys(smoothIterations);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				container.setElevation(x, y, container.getElevation(x,y) + elevation[x][y]);
			}
		}
	}

	private void renderValley(Mountain valley) {
		List<Position> corners = valley.getCorners();
		Position prevCorner = corners.get(corners.size() - 1);
		for (Iterator<Position> iterator = valley.getCorners().iterator(); iterator.hasNext(); ) {
			Position corner = iterator.next();
			Slope slope = new Slope(valley.getTop(), prevCorner, corner);
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
					float z = slope.getAltitude(x, y);
					if (elevation[x][y] > z && elevation[x][y] <= 0) {
						elevation[x][y] = z;
					}
				}
			}
		}
	}

	private void smoothValleys(int iterations) {
		float[][] innerElevationBuffer = new float[width][height];
		for (int i = 0; i < iterations; i++) {
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					innerElevationBuffer[x][y] = countMiddleElevation(x, y, smoothRadius);
				}
			}
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					elevation[x][y] = innerElevationBuffer[x][y];
				}
			}
		}
	}

	private float countMiddleElevation(int x, int y, int radius) {
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