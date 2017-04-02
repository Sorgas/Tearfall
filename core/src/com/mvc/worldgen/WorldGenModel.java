package com.mvc.worldgen;

import com.mvc.worldgen.generators.world.WorldGenFactory;
import com.mvc.worldgen.generators.world.GeneratorContainer;
import com.mvc.worldgen.generators.world.WorldGenConfig;
import com.mvc.worldgen.generators.world.WorldGenContainer;
import com.mvc.worldgen.generators.world.map_objects.WorldMap;
import com.utils.Position;
import com.mvc.GameModel;

/**
 * Created by Alexander on 06.03.2017.
 */
public class WorldGenModel implements GameModel {
	private WorldGenContainer worldGenContainer;
	private WorldMap map;
	private WorldGenView view;
	private GeneratorContainer generatorContainer;

	public WorldGenModel() {

	}

	public WorldGenContainer prepareSnapshot(Position camera) {
		return worldGenContainer;

	}

	private void generateWorld() {
		WorldGenFactory factory = WorldGenFactory.getInstance();
		WorldGenConfig config = new WorldGenConfig(500, 500 );
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

	public void setView(WorldGenView view) {
		this.view = view;
	}

	public WorldGenContainer getWorldGenContainer() {
		return worldGenContainer;
	}

	public WorldMap getMap() {
		return map;
	}
}