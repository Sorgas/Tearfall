package com.mvc.worldgen.generators.world;

import com.utils.Position;
import com.mvc.worldgen.generators.world.map_objects.WorldMap;
import com.mvc.worldgen.generators.world.world_objects.Edge;
import com.mvc.worldgen.generators.world.world_objects.Mountain;
import com.mvc.worldgen.generators.world.world_objects.Plate;
import com.mvc.worldgen.generators.world.world_objects.WorldCell;

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
	private List<Position> lakes;

	private float[][] elevation;
	private int[][] slopeAngles;
	private float[][] temperature;
	private float[][] debug;

	private float landPart;

	public WorldGenContainer(WorldGenConfig config) {
		this.width = config.getWidth();
		this.height = config.getHeight();
		this.config = config;
		reset();
	}

	public void fillMap() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				WorldCell cell= map.getCell(x,y);
				cell.setElevation(Math.round(elevation[x][y]));
				cell.setTemperature(temperature[x][y]);
			}
		}
	}

	public void setSlopeAngles(int x, int y, int value) {
		if (x >= 0 && x < width) {
			if (y >= 0 && y < height) {
				slopeAngles[x][y] = value;
			}
		}
	}

	public int getSlopeAngles(int x, int y) {
		int value = 0;
		if (x >= 0 && x < width) {
			if (y >= 0 && y < height) {
				value = slopeAngles[x][y];
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
		elevation = new float[width][height];
		slopeAngles = new int[width][height];
		temperature = new float[width][height];
		debug = new float[width][height];
		map = new WorldMap(width, height);
		plates = new ArrayList<>();
		edges = new ArrayList<>();
		mountains = new ArrayList<>();
		valleys = new ArrayList<>();
		hills = new ArrayList<>();
		lakes = new ArrayList<>();
	}

	public void setElevation(int x, int y, float value) {
		if (x >= 0 && x < width) {
			if (y >= 0 && y < height) {
				elevation[x][y] = value;
			}
		}
	}

	public float getElevation(int x, int y) {
		float value = 0;
		if (x >= 0 && x < width) {
			if (y >= 0 && y < height) {
				value = elevation[x][y];
			}
		}
		return value;
	}

	public void setDebug(int x, int y, float value) {
		if (x >= 0 && x < width) {
			if (y >= 0 && y < height) {
				debug[x][y] = value;
			}
		}
	}

	public float getDebug(int x, int y) {
		float value = 0;
		if (x >= 0 && x < width) {
			if (y >= 0 && y < height) {
				value = debug[x][y];
			}
		}
		return value;
	}

	public void setTemperature(int x, int y, float value) {
		if (x >= 0 && x < width) {
			if (y >= 0 && y < height) {
				temperature[x][y] = value;
			}
		}
	}

	public float getTemperature(int x, int y) {
		float value = 0;
		if (x >= 0 && x < width) {
			if (y >= 0 && y < height) {
				value = temperature[x][y];
			}
		}
		return value;
	}

	public List<Position> getLakes() {
		return lakes;
	}

	public float getLandPart() {
		return landPart;
	}

	public void setLandPart(float landPart) {
		this.landPart = landPart;
	}
}
