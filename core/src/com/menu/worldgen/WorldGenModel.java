package com.menu.worldgen;

import com.TearFall;
import com.torefine.GameModel;
import com.menu.worldgen.generators.world.*;

import java.util.Random;

/**
 * Created by Alexander on 06.03.2017.
 */
public class WorldGenModel implements GameModel {
	private WorldGenContainer worldGenContainer;
	private GeneratorContainer generatorContainer;
	private WorldMap map;
	private long seed;

	private WorldGenView view;
	private TearFall game;

	private int worldSize = 100;


	private Random random;

	public WorldGenModel(TearFall game) {
		this.game = game;
		random = new Random();
		seed = random.nextLong();
		init();
	}

	public void generateWorld() {
		WorldGenFactory factory = WorldGenFactory.getInstance();
		WorldGenConfig config = new WorldGenConfig(worldSize, worldSize);
		factory.initMapContainer(config);
		generatorContainer = factory.getGeneratorContainer();
		generatorContainer.runContainer();
		worldGenContainer = generatorContainer.getWorldGenContainer();
		map = worldGenContainer.getMap();
		System.out.println(map);
	}

	@Override
	public void init() {

	}

	public void setView(WorldGenView view) {
		this.view = view;
	}

	public WorldGenContainer getWorldGenContainer() {
		return worldGenContainer;
	}

	public WorldMap getMap() {
		return map;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}

	public long getSeed() {
		return seed;
	}

	public int getWorldSize() {
		return worldSize;
	}

	public void setWorldSize(int worldSize) {
		this.worldSize = worldSize;
	}
}