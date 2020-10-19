package stonering.generators.localgen;

import stonering.entity.world.World;
import stonering.game.model.MainGameModel;
import stonering.game.model.local_map.LocalMap;
import stonering.generators.localgen.generators.*;
import stonering.generators.localgen.generators.flora.LocalForestGenerator;
import stonering.generators.localgen.generators.flora.LocalPlantsGenerator;
import stonering.generators.localgen.generators.flora.LocalSubstrateGenerator;

/**
 * Executes local generators in correct order. During generation {@link MainGameModel} is filled.
 *
 * @author Alexander Kuzyakov on 27.08.2017.
 */
public class LocalGeneratorContainer {
    private LocalGenContainer localGenContainer;
    private LocalElevationGenerator localElevationGenerator;
    private LocalStoneLayersGenerator localStoneLayersGenerator;
    private LocalRiverGenerator localRiverGenerator;
    private LocalCaveGenerator localCaveGenerator;
    private LocalRampAndFloorPlacer localRampAndFloorPlacer;
    private LocalPlantsGenerator localPlantsGenerator;
    private LocalForestGenerator localForestGenerator;
    private LocalSubstrateGenerator localSubstrategenerator;
    private LocalFaunaGenerator localFaunaGenerator;
    private LocalBuildingGenerator localBuildingGenerator;
    private LocalItemsGenerator localItemsGenerator;
    private LocalTemperatureGenerator localTemperatureGenerator;
    private LocalSurfaceWaterPoolsGenerator localSurfaceWaterPoolsGenerator;
    private LocalOresGenerator localOresGenerator;
    private LocalGenConfig config;

    public LocalGeneratorContainer(LocalGenConfig config, World world) {
        this.config = config;
        localGenContainer = new LocalGenContainer(config, world);
        createGenerators();
    }

    private void createGenerators() {
        localElevationGenerator = new LocalElevationGenerator(localGenContainer);
        localStoneLayersGenerator = new LocalStoneLayersGenerator(localGenContainer);
//        localOresGenerator = new
        localCaveGenerator = new LocalCaveGenerator(localGenContainer);
        localRampAndFloorPlacer = new LocalRampAndFloorPlacer(localGenContainer);
        localTemperatureGenerator = new LocalTemperatureGenerator(localGenContainer);
        localFaunaGenerator = new LocalFaunaGenerator(localGenContainer);
        localBuildingGenerator = new LocalBuildingGenerator(localGenContainer);
        localItemsGenerator = new LocalItemsGenerator(localGenContainer);
        localPlantsGenerator = new LocalPlantsGenerator(localGenContainer);
        localForestGenerator = new LocalForestGenerator(localGenContainer);
        localSubstrategenerator = new LocalSubstrateGenerator(localGenContainer);
        localSurfaceWaterPoolsGenerator = new LocalSurfaceWaterPoolsGenerator(localGenContainer);
        localRiverGenerator = new LocalRiverGenerator(localGenContainer);
    }

    public void execute() {
        //landscape
        localElevationGenerator.execute(); //creates heights map
        localStoneLayersGenerator.execute(); //fills localmap with blocks by heightsmap
        localCaveGenerator.execute(); //digs caves
        //water
//        localRiverGenerator.execute(); // carves river beds
        localSurfaceWaterPoolsGenerator.execute(); // digs ponds
        localRampAndFloorPlacer.execute(); // places floors and ramps upon all top blocks
        //plants
        localTemperatureGenerator.execute(); // generates year temperature cycle
        localForestGenerator.execute(); // places trees
        localPlantsGenerator.execute(); // places plants
        localSubstrategenerator.execute(); // places substrates
        //creatures
        localFaunaGenerator.execute(); // places animals
        //buildings
        localBuildingGenerator.execute();
        //item
        localItemsGenerator.execute(); // places item
    }

    public MainGameModel getGameModel() {
        return localGenContainer.model;
    }

    public LocalMap getLocalMap() {
        return localGenContainer.model.get(LocalMap.class);
    }
}
