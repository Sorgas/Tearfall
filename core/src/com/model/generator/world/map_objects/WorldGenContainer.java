package com.model.generator.world.map_objects;

import com.model.generator.world.world_objects.Edge;
import com.model.generator.world.world_objects.Mountain;
import com.model.generator.world.world_objects.Plate;
import com.model.generator.world.world_objects.WorldCell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 05.03.2017.
 */
public class WorldGenContainer {
	private WorldGenConfig config;
	private int width;
	private int height;
	private WorldMap map;

	private List<Plate> plates;
	private List<Edge> edges;
	private List<Mountain> mountains;
	private List<Mountain> valleys;
	private List<Mountain> hills;

	private int[][] mountainElevation;
	private int[][] valleyElevation;
	private int[][] hillElevation;

	public WorldGenContainer(WorldGenConfig config) {
		this.width = config.getWidth();
		this.height = config.getHeight();
		this.config = config;
		reset();
	}

	public void fillMap() {
		addMountains();
		addValleys();
		addHills();
	}

	private void addMountains() {
		for (int x = 0; x < mountainElevation.length; x++) {
			for (int y = 0; y < mountainElevation[x].length; y++) {
				WorldCell cell = map.getCell(x, y);
				cell.setElevation(cell.getElevation() + mountainElevation[x][y]);
			}
		}
	}

	private void addValleys() {
		for (int x = 0; x < valleyElevation.length; x++) {
			for (int y = 0; y < valleyElevation[x].length; y++) {
				WorldCell cell = map.getCell(x, y);
				cell.setElevation(cell.getElevation() + valleyElevation[x][y]);
			}
		}
	}

	private void addHills() {
		for (int x = 0; x < hillElevation.length; x++) {
			for (int y = 0; y < hillElevation[x].length; y++) {
				WorldCell cell = map.getCell(x, y);
				cell.setElevation(cell.getElevation() + hillElevation[x][y]);
			}
		}
	}

	public void setMountainElevation(int x, int y, int value) {
		if (x >= 0 && x < width) {
			if (y >= 0 && y < height) {
				mountainElevation[x][y] = value;
			}
		}
	}

	public void setValleyElevation(int x, int y, int value) {
		if (x >= 0 && x < width) {
			if (y >= 0 && y < height) {
				valleyElevation[x][y] = value;
			}
		}
	}

	public void setHillElevation(int x, int y, int value) {
		if (x >= 0 && x < width) {
			if (y >= 0 && y < height) {
				hillElevation[x][y] = value;
			}
		}
	}

	public int getMountainElevation(int x, int y) {
		int value = 0;
		if (x >= 0 && x < width) {
			if (y >= 0 && y < height) {
				value = mountainElevation[x][y];
			}
		}
		return value;
	}

	public int getValleyElevation(int x, int y) {
		int value = 0;
		if (x >= 0 && x < width) {
			if (y >= 0 && y < height) {
				value = valleyElevation[x][y];
			}
		}
		return value;
	}

	public int getHillElevation(int x, int y) {
		int value = 0;
		if (x >= 0 && x < width) {
			if (y >= 0 && y < height) {
				value = hillElevation[x][y];
			}
		}
		return value;
	}

	public List<Plate> getPlates() {
		return plates;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public List<Mountain> getHills() {
		return hills;
	}

	public List<Mountain> getMountains() {
		return mountains;
	}

	public WorldGenConfig getConfig() {
		return config;
	}

	public WorldMap getMap() {
		return map;
	}

	public void reset() {
		mountainElevation = new int[width][height];
		valleyElevation = new int[width][height];
		hillElevation = new int[width][height];
		map = new WorldMap(width, height);
		plates = new ArrayList<>();
		edges = new ArrayList<>();
		mountains = new ArrayList<>();
		valleys = new ArrayList<>();
		hills = new ArrayList<>();
	}
}
