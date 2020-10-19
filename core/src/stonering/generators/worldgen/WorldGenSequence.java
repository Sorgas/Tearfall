package stonering.generators.worldgen;

import stonering.entity.world.World;
import stonering.generators.worldgen.generators.BiomeGenerator;
import stonering.generators.worldgen.generators.CelestialBodiesGenerator;
import stonering.generators.worldgen.generators.drainage.*;
import stonering.generators.worldgen.generators.elevation.*;
import stonering.generators.worldgen.generators.temperature.TemperatureGenerator;

/**
 * Holds world generators and runs them in a right order.
 */
public class WorldGenSequence {
    public WorldGenContainer container; // container for generation intermediate results 
    public WorldGenConfig config;
    
    private ElevationGenerator elevationGenerator;
    private OceanFiller oceanFiller;
    private TemperatureGenerator temperatureGenerator;
    private RainfallGenerator rainfallGenerator;
    private ErosionGenerator erosionGenerator;
    private ElevationModifier elevationModifier;
    private RiverGenerator riverGenerator;
    private LakeGenerator lakeGenerator;
    private BrookGenerator brookGenerator;
    private DrainageGenerator drainageGenerator;
    private BiomeGenerator biomeGenerator;
    private CelestialBodiesGenerator celestialBodiesGenerator;

    public WorldGenSequence(WorldGenConfig config) {
        container = new WorldGenContainer(config);
        oceanFiller = new OceanFiller(container);
        riverGenerator = new RiverGenerator(container);
        brookGenerator = new BrookGenerator(container);
        temperatureGenerator = new TemperatureGenerator(container);
        elevationGenerator = new ElevationGenerator(container);
        rainfallGenerator = new RainfallGenerator(container);
        erosionGenerator = new ErosionGenerator(container);
        elevationModifier = new ElevationModifier(container);
        lakeGenerator = new LakeGenerator(container);
        drainageGenerator = new DrainageGenerator(container);
        biomeGenerator = new BiomeGenerator(container);
        celestialBodiesGenerator = new CelestialBodiesGenerator(container);
    }

    public void runGenerators() {
        celestialBodiesGenerator.execute();
        elevationGenerator.execute();
        container.fillMap();
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
        container.fillMap();
    }

    /**
     * Returns world map from container.
     */
    public World getWorld() {
        return container.world;
    }
}