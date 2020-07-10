package stonering.generators.worldgen;

import stonering.entity.world.World;
import stonering.generators.worldgen.generators.BiomeGenerator;
import stonering.generators.worldgen.generators.CelestialBodiesGenerator;
import stonering.generators.worldgen.generators.drainage.*;
import stonering.generators.worldgen.generators.elevation.*;
import stonering.generators.worldgen.generators.elevation.PlateGenerator;
import stonering.generators.worldgen.generators.temperature.TemperatureGenerator;

/**
 * Holds world generators and runs them in a right order.
 */
public class WorldGeneratorContainer {
    private WorldGenContainer worldGenContainer; // container for generation intermediate results 
    
    private OceanFiller oceanFiller;
    private TemperatureGenerator temperatureGenerator;
    private ElevationGenerator elevationGenerator;
    private RainfallGenerator rainfallGenerator;
    private ErosionGenerator erosionGenerator;
    private ElevationModifier elevationModifier;
    private RiverGenerator riverGenerator;
    private LakeGenerator lakeGenerator;
    private BrookGenerator brookGenerator;
    private DrainageGenerator drainageGenerator;
    private BiomeGenerator biomeGenerator;
    private CelestialBodiesGenerator celestialBodiesGenerator;

    public WorldGeneratorContainer(WorldGenConfig config) {
        worldGenContainer = new WorldGenContainer(config);
        oceanFiller = new OceanFiller(worldGenContainer);
        riverGenerator = new RiverGenerator(worldGenContainer);
        brookGenerator = new BrookGenerator(worldGenContainer);
        temperatureGenerator = new TemperatureGenerator(worldGenContainer);
        elevationGenerator = new ElevationGenerator(worldGenContainer);
        rainfallGenerator = new RainfallGenerator(worldGenContainer);
        erosionGenerator = new ErosionGenerator(worldGenContainer);
        elevationModifier = new ElevationModifier(worldGenContainer);
        lakeGenerator = new LakeGenerator(worldGenContainer);
        drainageGenerator = new DrainageGenerator(worldGenContainer);
        biomeGenerator = new BiomeGenerator(worldGenContainer);
        celestialBodiesGenerator = new CelestialBodiesGenerator(worldGenContainer);
    }

    public void runContainer() {
        boolean rejected;
        do {
            rejected = runGenerators();
            if (rejected) {
                worldGenContainer.reset();
            }
        } while (rejected);
    }

    private boolean runGenerators() {
        celestialBodiesGenerator.execute();
        elevationGenerator.execute();
        worldGenContainer.fillMap();
        oceanFiller.execute();
        erosionGenerator.execute();
        temperatureGenerator.execute();
        rainfallGenerator.execute();
//        elevationModifier.execute();
        riverGenerator.execute();
//        brookGenerator.execute();
//        lakeGenerator.execute();
//        drainageGenerator.execute();
//        biomeGenerator.execute();
        worldGenContainer.fillMap();
        return false;
    }

    /**
     * Returns world map from container.
     */
    public World getWorld() {
        return worldGenContainer.getWorld();
    }
}