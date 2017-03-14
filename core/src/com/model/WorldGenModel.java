package com.model;

import com.model.generator.world.WorldGenFactory;
import com.model.generator.world.generators.GeneratorContainer;
import com.model.generator.world.map_objects.WorldGenConfig;
import com.model.generator.world.map_objects.WorldGenContainer;
import com.model.generator.world.map_objects.WorldMap;
import com.model.utils.Position;
import com.view.WorldView;

/**
 * Created by Alexander on 06.03.2017.
 */
public class WorldGenModel implements GameModel {
	private WorldGenContainer worldGenContainer;
	private WorldMap map;
	private WorldView view;
	private GeneratorContainer generatorContainer;

	public WorldGenModel() {

	}

	public WorldGenContainer prepareSnapshot(Position camera) {
		return worldGenContainer;

	}

	private void generateWorld() {
		WorldGenFactory factory = WorldGenFactory.getInstance();
		WorldGenConfig config = new WorldGenConfig(200, 200);
		factory.initMapContainer(config);
		generatorContainer = factory.getGeneratorContainer();
		generatorContainer.runContainer();
		worldGenContainer = generatorContainer.getWorldGenContainer();
		map = worldGenContainer.getMap();
	}

	@Override
	public void init() {
		generateWorld();
	}

	@Override
	public void showFrame() {
		view.showFrame();
	}

	public void setView(WorldView view) {
		this.view = view;
	}

	public WorldGenContainer getWorldGenContainer() {
		return worldGenContainer;
	}

	public WorldMap getMap() {
		return map;
	}
}