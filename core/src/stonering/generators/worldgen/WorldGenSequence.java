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
    public final WorldGenConfig config;
    
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
        this.config = config;
        oceanFiller = new OceanFiller();
        riverGenerator = new RiverGenerator();
        brookGenerator = new BrookGenerator();
        temperatureGenerator = new TemperatureGenerator();
        elevationGenerator = new ElevationGenerator();
        rainfallGenerator = new RainfallGenerator();
        erosionGenerator = new ErosionGenerator();
        elevationModifier = new ElevationModifier();
        lakeGenerator = new LakeGenerator();
        drainageGenerator = new DrainageGenerator();
        biomeGenerator = new BiomeGenerator();
        celestialBodiesGenerator = new CelestialBodiesGenerator();
    }

    public void runGenerators() {
        container = new WorldGenContainer(config);
        celestialBodiesGenerator.execute(container);
        elevationGenerator.execute(container);
        container.fillMap();
        oceanFiller.execute(container);
        erosionGenerator.execute(container);
        temperatureGenerator.execute(container);
        rainfallGenerator.execute(container);
//        elevationModifier.execute(container);
        riverGenerator.execute(container);
//        brookGenerator.execute(container);
//        lakeGenerator.execute(container);
//        drainageGenerator.execute(container);
//        biomeGenerator.execute(container);
        container.fillMap();
    }
}