package stonering.generators.localgen;

import stonering.game.core.model.LocalMap;
import stonering.generators.localgen.generators.*;
import stonering.generators.worldgen.WorldMap;

/**
 * @author Alexander Kuzyakov on 27.08.2017.
 *         <p>
 *         executes local generators in correct order
 */
public class LocalGeneratorContainer {
    private LocalGenContainer localGenContainer;

    private LocalHeightsGenerator localHeightsGenerator;
    private LocalStoneLayersGenerator localStoneLayersGenerator;
    private LocalRiverGenerator localRiverGenerator;
    private LocalCaveGenerator localCaveGenerator;
    private LocalRampAndFloorPlacer localRampAndFloorPlacer;
    private LocalFloraGenerator localFloraGenerator;
    private LocalFaunaGenerator localFaunaGenerator;
    private LocalFurnitureGenerator localFurnitureGenerator;
    private LocalItemsGenerator localItemsGenerator;
    private LocalTemperatureGenerator localTemperatureGenerator;
    private LocalSurfaceWaterPoolsGenerator localSurfaceWaterPoolsGenerator;
    private WorldMap world;
    private LocalGenConfig config;

    public LocalGeneratorContainer(LocalGenConfig config, WorldMap world) {
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
        localFurnitureGenerator = new LocalFurnitureGenerator(localGenContainer);
        localItemsGenerator = new LocalItemsGenerator(localGenContainer);
        localFloraGenerator = new LocalFloraGenerator(localGenContainer);
        localSurfaceWaterPoolsGenerator = new LocalSurfaceWaterPoolsGenerator(localGenContainer);
        localRiverGenerator = new LocalRiverGenerator(localGenContainer);
    }

    public void execute() {
        localGenContainer.initContainer();
        localHeightsGenerator.execute(); //creates heights map
        localStoneLayersGenerator.execute(); //fills localmap with blocks by heightsmap
        localCaveGenerator.execute(); //digs caves
        localRiverGenerator.execute(); // carves river beds
        localSurfaceWaterPoolsGenerator.execute(); // digs ponds
        localRampAndFloorPlacer.execute(); // places floors and ramps upon all top blocks
        localTemperatureGenerator.execute(); // generates year temperature cycle
//        localFloraGenerator.execute(); // places trees and plants
//        localFaunaGenerator.execute(); // places animals
////        localFurnitureGenerator.execute();
//        localItemsGenerator.execute(); // places items
    }

    public LocalMap getLocalMap() {
        return localGenContainer.getLocalMap();
    }

    public void setWorld(WorldMap world) {
        this.world = world;
    }

    public LocalGenContainer getLocalGenContainer() {
        return localGenContainer;
    }
}
