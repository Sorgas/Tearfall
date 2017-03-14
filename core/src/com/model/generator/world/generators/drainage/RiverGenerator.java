package com.model.generator.world.generators.drainage;

import com.model.generator.world.map_objects.WorldGenContainer;

import java.util.Random;

/**
 * Created by Alexander on 14.03.2017.
 */
public class RiverGenerator {
	private final WorldGenContainer container;
	private Random random;
	private int width;
	private int height;
	private int RiverCount;
	private int[][] slopeAngles;


	public RiverGenerator(WorldGenContainer container) {
		this.container = container;
		random = container.getConfig().getRandom();
		width = container.getConfig().getWidth();
		height = container.getConfig().getHeight();
	}

	public void execute() {

	}

	private void initSlopeAngles() {

	}

	private float countSlopeAngle(int x, int y) {
		int minX = x - 1;
		int maxX = x + 2;
		int minY = y - 1;
		int maxY = y + 2;
		int xWidth = 3;
		int yWidth = 3;
		float sum = 0;

		if (minX < 0) {
			xWidth -= 1;
			minX = 0;
		}
		if (maxX > width) {
			xWidth -= 1;
			maxX = width;
		}
		if (minY < 0) {
			yWidth -= 1;
			minY = 0;
		}
		if (maxY > height) {
			yWidth -= 1;
			maxY = height;
		}
		for (int i = minX; i < maxX; i++) {
			for (int j = minY; j < maxY; j++) {

			}
		}
		return sum / (xWidth * yWidth);
	}
}
