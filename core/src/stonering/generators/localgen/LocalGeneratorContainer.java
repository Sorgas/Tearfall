package stonering.generators.localgen;

import stonering.game.core.model.LocalMap;
import stonering.generators.localgen.generators.*;
import stonering.generators.worldgen.WorldMap;

/**
 * Created by Alexander on 27.08.2017.
 *
 * executes local generators in correct order
 */
public class LocalGeneratorContainer {
    private LocalGenContainer localGenContainer;

    private LocalHeightsGenerator localHeightsGenerator;
    private LocalStoneLayersGenerator localStoneLayersGenerator;
    private LocalRiverGenerator riverGenerator;
    private LocalCaveGenerator localCaveGenerator;
    private LocalRampAndFloorPlacer localRampAndFloorPlacer;
    private LocalFloraGenerator localFloraGenerator;
    private LocalFaunaGenerator localFaunaGenerator;
    private LocalFurnitureGenerator localFurnitureGenerator;
    private LocalItemsGenerator localItemsGenerator;
    private LocalTemperatureGenerator localTemperatureGenerator;
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
    }

    public void execute() {
        localGenContainer.initContainer();
        localHeightsGenerator.execute();
        localStoneLayersGenerator.execute();
        localCaveGenerator.execute();
        localRampAndFloorPlacer.execute();
        localTemperatureGenerator.execute();
        localFloraGenerator.execute();
//        localFaunaGenerator.execute();
//        localFurnitureGenerator.execute();
//        localItemsGenerator.execute();
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
