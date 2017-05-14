package com.menu.worldgen.generators.world;

import com.menu.worldgen.generators.world.generators.*;
import com.menu.worldgen.generators.world.generators.drainage.OceanFiller;
import com.menu.worldgen.generators.world.generators.drainage.RiverGenerator;
import com.menu.worldgen.generators.world.generators.elevation.*;
import com.menu.worldgen.generators.world.generators.temperature.TemperatureGenerator;

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
		generatorContainer.setRiverGenerator(new RiverGenerator(worldGenContainer));
		generatorContainer.setTemperatureGenerator(new TemperatureGenerator(worldGenContainer));
		generatorContainer.setElevationGenerator(new ElevationGenerator(worldGenContainer));
		generatorContainer.setDiamondSquareGenerator(new DiamondSquareGenerator(worldGenContainer));
	}

	public GeneratorContainer getGeneratorContainer() {
		return generatorContainer;
	}
}