package com.model.generator.world;

import com.model.generator.world.generators.*;
import com.model.generator.world.generators.drainage.OceanFiller;
import com.model.generator.world.generators.elevation.*;
import com.model.generator.world.map_objects.WorldGenConfig;
import com.model.generator.world.map_objects.WorldGenContainer;

/**
 * Created by Alexander on 06.03.2017.
 */
public class WorldGenFactory {

	private static WorldGenFactory instance = null;
	WorldGenConfig config;

	private WorldGenContainer worldGenContainer;
	private GeneratorContainer generatorContainer;

	public static WorldGenFactory getInstance() {
		if(instance == null) {
			instance = new WorldGenFactory();
		}
		return instance;
	}

	public GeneratorContainer initMapContainer(WorldGenConfig config) {
		this.config = config;
		worldGenContainer = new WorldGenContainer(config);
		initGenerators();
		return generatorContainer;
	}

	private void initGenerators() {
		generatorContainer = new GeneratorContainer();
		generatorContainer.setWorldGenContainer(worldGenContainer);
		generatorContainer.setPlateGenerator(new PlateGenerator(worldGenContainer));
		generatorContainer.setMountainGenerator(new MountainGenerator(worldGenContainer));
		generatorContainer.setValleyGenerator(new ValleyGenerator(worldGenContainer));
		generatorContainer.setHillGenerator(new HillGenerator(worldGenContainer));

		generatorContainer.setMountainRenderer(new MountainRenderer(worldGenContainer));
		generatorContainer.setValleyRenderer(new ValleyRenderer(worldGenContainer));
		generatorContainer.setHillRenderer(new HillRenderer(worldGenContainer));
		generatorContainer.setOceanFiller(new OceanFiller(worldGenContainer));
	}

	public GeneratorContainer getGeneratorContainer() {
		return generatorContainer;
	}
}