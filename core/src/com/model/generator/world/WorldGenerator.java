package com.model.generator.world;


import com.model.generator.world.world_objects.Edge;
import com.model.generator.world.world_objects.Plate;
import com.model.utils.Position;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class WorldGenerator {
	private int width;
	private int height;
	private boolean rejected;

	//for plate generator
	private int plateNum = 12;

	// for mountain generator
	// minimal distance between tops
	private int topsDensity = 10;
	// multiplier of plate speed
	private int PlateSpeedToHeightModifier = 3;

	private WorldMap map;
	private Random rand;
	private List<Plate> plates;
	private List<Edge> edges;
	private List<Position> elevationTops;

	public WorldGenerator() {
		rand = new Random();
		plates = new ArrayList<>();
		edges = new ArrayList<>();
		elevationTops = new ArrayList<>();
	}

	public void createMap(int width, int height) {
		this.width = width;
		this.height = height;
		rejected = true;
		int rejectCount;
		for (rejectCount = 0; rejected == true; rejectCount++) {
			rejected = false;
			runSequense();
		}
		System.out.println("rejected:  " + (rejectCount - 1));
	}

	private void runSequense() {
		map = new WorldMap(width, height);
		PlateGenerator plateGenerator = new PlateGenerator(width, height, rand);
		plateGenerator.generatePlates(plateNum);
		plates = plateGenerator.getPlates();
		edges = plateGenerator.getEdges();
		rejected = plateGenerator.isRejected();
		if (!rejected) {
			MountainGenerator mountainGenerator = new MountainGenerator(width, height, rand, edges);
			mountainGenerator.setPlateSpeedToHeightModifier(PlateSpeedToHeightModifier);
			mountainGenerator.setTopsDensity(topsDensity);
			mountainGenerator.generateMountains();
		}
	}

	public WorldMap getMap() {
		return map;
	}

	public List<Plate> getPlates() {
		return plates;
	}

	public List<Position> getFocuses() {
		List<Position> list = new ArrayList<>();
		for (Iterator<Plate> iterator = plates.iterator(); iterator.hasNext(); ) {
			list.add(iterator.next().getCenter());
		}
		return list;
	}

	public List<Edge> getEdges() {
		return edges;
	}
}