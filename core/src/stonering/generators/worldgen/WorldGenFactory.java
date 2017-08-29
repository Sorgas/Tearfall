package stonering.generators.worldgen;

import stonering.generators.worldgen.generators.drainage.RainfallGenerator;
import stonering.generators.worldgen.generators.elevation.*;
import stonering.generators.worldgen.generators.drainage.OceanFiller;
import stonering.generators.worldgen.generators.drainage.RiverGenerator;
import stonering.generators.worldgen.generators.temperature.TemperatureGenerator;
import stonering.generators.worldgen.generators.PlateGenerator;

/**
 * Created by Alexander on 06.03.2017.
 */
public class WorldGenFactory {

	private static WorldGenFactory instance = null;
	WorldGenConfig config;

	private WorldGenContainer worldGenContainer;
	private GlobalGeneratorContainer globalGeneratorContainer;

	public static WorldGenFactory getInstance() {
		if(instance == null) {
			instance = new WorldGenFactory();
		}
		return instance;
	}

	public GlobalGeneratorContainer initMapContainer(WorldGenConfig config) {
		this.config = config;
		worldGenContainer = new WorldGenContainer(config);
		initGenerators();
		return globalGeneratorContainer;
	}

	private void initGenerators() {
		globalGeneratorContainer = new GlobalGeneratorContainer();
		globalGeneratorContainer.setWorldGenContainer(worldGenContainer);
		globalGeneratorContainer.setPlateGenerator(new PlateGenerator(worldGenContainer));
		globalGeneratorContainer.setMountainGenerator(new MountainGenerator(worldGenContainer));
		globalGeneratorContainer.setValleyGenerator(new ValleyGenerator(worldGenContainer));
		globalGeneratorContainer.setHillGenerator(new HillGenerator(worldGenContainer));

		globalGeneratorContainer.setMountainRenderer(new MountainRenderer(worldGenContainer));
		globalGeneratorContainer.setValleyRenderer(new ValleyRenderer(worldGenContainer));
		globalGeneratorContainer.setHillRenderer(new HillRenderer(worldGenContainer));
		globalGeneratorContainer.setOceanFiller(new OceanFiller(worldGenContainer));
		globalGeneratorContainer.setRiverGenerator(new RiverGenerator(worldGenContainer));
		globalGeneratorContainer.setTemperatureGenerator(new TemperatureGenerator(worldGenContainer));
		globalGeneratorContainer.setElevationGenerator(new ElevationGenerator(worldGenContainer));
		globalGeneratorContainer.setDiamondSquareGenerator(new DiamondSquareGenerator(worldGenContainer));
		globalGeneratorContainer.setRainfallGenerator(new RainfallGenerator(worldGenContainer));
	}

	public GlobalGeneratorContainer getGlobalGeneratorContainer() {
		return globalGeneratorContainer;
	}
}