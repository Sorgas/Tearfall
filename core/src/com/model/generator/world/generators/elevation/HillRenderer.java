package com.model.generator.world.generators.elevation;

import com.model.generator.world.map_objects.WorldGenContainer;
import com.model.generator.world.world_objects.Mountain;
import com.model.generator.world.world_objects.Slope;
import com.model.utils.Position;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Alexander on 12.03.2017.
 */
public class HillRenderer {
	private WorldGenContainer container;
	private int width;
	private int height;
	private float[][] elevationBuffer;
	private int smoothIterations = 1;
	private int smoothRadius = 1;

	public HillRenderer(WorldGenContainer container) {
		this.container = container;
		this.width = container.getConfig().getWidth();
		this.height = container.getConfig().getHeight();
		elevationBuffer = new float[width][height];
	}

	public void execute() {
		for (Iterator<Mountain> iterator = container.getHills().iterator(); iterator.hasNext(); ) {
			Mountain hill = iterator.next();
			renderHill(hill);
			smoothHills(smoothIterations);
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
					float z = slope.getAltitude(x, y);
					container.setHillElevation(x, y, Math.round(z));
				}
			}
		}
	}

	private void smoothHills(int iterations) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				elevationBuffer[x][y] = container.getHillElevation(x, y);
			}
		}
		float[][] innerElevationBuffer = new float[width][height];
		for (int i = 0; i < iterations; i++) {
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					innerElevationBuffer[x][y] = countMiddleElevation(x, y, smoothRadius, true);
				}
			}
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					elevationBuffer[x][y] = innerElevationBuffer[x][y];
				}
			}
		}
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
					container.setHillElevation(x, y, Math.round(elevationBuffer[x][y]));
			}
		}
	}

	private float countMiddleElevation(int x, int y, int radius, boolean lockBorders) {
		int minX = x - radius;
		int maxX = x + radius + 1;
		int minY = y - radius;
		int maxY = y + radius + 1;
		int xWidth = 2 * radius + 1;
		int yWidth = 2 * radius + 1;
		float sum = 0;

		if (minX < 0) {
			if (!lockBorders) {
				xWidth += minX;
				minX = 0;
			} else {
				return elevationBuffer[x][y];
			}
		}
		if (maxX > width) {
			if (!lockBorders) {

				xWidth -= maxX - width;
				maxX = width;
			} else {
				return elevationBuffer[x][y];
			}
		}
		if (minY < 0) {
			if (!lockBorders) {
				yWidth += minY;
				minY = 0;
			} else {
				return elevationBuffer[x][y];
			}
		}
		if (maxY > height) {
			if (!lockBorders) {
				yWidth -= maxY - height;
				maxY = height;
			} else {
				return elevationBuffer[x][y];
			}
		}
		for (int i = minX; i < maxX; i++) {
			for (int j = minY; j < maxY; j++) {
				sum += elevationBuffer[i][j];
			}
		}
		return sum / (xWidth * yWidth);
	}

	private float countPoorMiddleElevation(int x, int y) {
		int count = 1;
		float sum = elevationBuffer[x][y];
		if (x > 0) {
			sum += elevationBuffer[x - 1][y];
			count++;
		}
		if (x < width - 1) {
			sum += elevationBuffer[x + 1][y];
			count++;
		}
		if (y > 0) {
			sum += elevationBuffer[x][y - 1];
			count++;
		}
		if (y < height - 1) {
			sum += elevationBuffer[x][y + 1];
			count++;
		}
		return sum / count;
	}
}