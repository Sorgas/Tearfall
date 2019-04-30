package stonering.generators.localgen;

import stonering.entity.world.World;
import stonering.game.model.local_map.LocalMap;
import stonering.generators.localgen.generators.*;
import stonering.generators.localgen.generators.flora.LocalFloraGenerator;
import stonering.generators.localgen.generators.flora.LocalPlantsGenerator;

/**
 * Executes local generators in correct order.
 *
 * @author Alexander Kuzyakov on 27.08.2017.
 */
public class LocalGeneratorContainer {
    private LocalGenContainer localGenContainer;

    private LocalHeightsGenerator localHeightsGenerator;
    private LocalStoneLayersGenerator localStoneLayersGenerator;
    private LocalRiverGenerator localRiverGenerator;
    private LocalCaveGenerator localCaveGenerator;
    private LocalRampAndFloorPlacer localRampAndFloorPlacer;
    private LocalPlantsGenerator localPlantsGenerator;
    private LocalFaunaGenerator localFaunaGenerator;
    private LocalBuildingGenerator localBuildingGenerator;
    private LocalItemsGenerator localItemsGenerator;
    private LocalTemperatureGenerator localTemperatureGenerator;
    private LocalSurfaceWaterPoolsGenerator localSurfaceWaterPoolsGenerator;
    private World world;
    private LocalGenConfig config;

    public LocalGeneratorContainer(LocalGenConfig config, World world) {
        this.world = world;
        this.config = config;
        init();
    }

    public void init() {
        localGenContainer = new LocalGenContainer(config, world);
        localHeightsGenerator = new LocalHeightsGenerator(localGenContainer);
        localStoneLayersGenerator = new LocalStoneLayersGenerator(localGenContainer);
        localCaveGenerator = new LocalCaveGenerator(localGenContainer);
        localRampAndFloorPlacer = new LocalRampAndFloorPlacer(localGenContainer);
        localTemperatureGenerator = new LocalTemperatureGenerator(localGenContainer);
        localFaunaGenerator = new LocalFaunaGenerator(localGenContainer);
        localBuildingGenerator = new LocalBuildingGenerator(localGenContainer);
        localItemsGenerator = new LocalItemsGenerator(localGenContainer);
        localPlantsGenerator = new LocalPlantsGenerator(localGenContainer);
        localSurfaceWaterPoolsGenerator = new LocalSurfaceWaterPoolsGenerator(localGenContainer);
        localRiverGenerator = new LocalRiverGenerator(localGenContainer);
    }

    public void execute() {
        localGenContainer.initContainer();
        localHeightsGenerator.execute(); //creates heights map
        localStoneLayersGenerator.execute(); //fills localmap with blocks by heightsmap
        localCaveGenerator.execute(); //digs caves
//        localRiverGenerator.execute(); // carves river beds
        localSurfaceWaterPoolsGenerator.execute(); // digs ponds
        localRampAndFloorPlacer.execute(); // places floors and ramps upon all top blocks

        localTemperatureGenerator.execute(); // generates year temperature cycle
        localPlantsGenerator.execute(); // places trees and plants
        localFaunaGenerator.execute(); // places animals
        localBuildingGenerator.execute();
        localItemsGenerator.execute(); // places items
    }

    public LocalMap getLocalMap() {
        return localGenContainer.localMap;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public LocalGenContainer getLocalGenContainer() {
        return localGenContainer;
    }
}
