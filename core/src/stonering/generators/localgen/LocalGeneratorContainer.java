package stonering.generators.localgen;

import stonering.game.core.model.LocalMap;
import stonering.generators.localgen.generators.*;
import stonering.generators.worldgen.WorldMap;
import stonering.global.utils.Position;

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
    private LocalForestGenerator localForestGenerator;
    private LocalRampPlacer localRampPlacer;
    private LocalFaunaGenerator localFaunaGenerator;
    private LocalFurnitureGenerator localFurnitureGenerator;
    private LocalFloorPlacer localFloorPlacer;
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
        localRampPlacer = new LocalRampPlacer(localGenContainer);
        localTemperatureGenerator = new LocalTemperatureGenerator(localGenContainer);
        localForestGenerator = new LocalForestGenerator(localGenContainer);
        localFaunaGenerator = new LocalFaunaGenerator(localGenContainer);
        localFurnitureGenerator = new LocalFurnitureGenerator(localGenContainer);
        localFloorPlacer = new LocalFloorPlacer(localGenContainer);
        localItemsGenerator = new LocalItemsGenerator(localGenContainer);

    }

    public void execute() {
        localHeightsGenerator.execute();
        localStoneLayersGenerator.execute();
        localCaveGenerator.execute();
        localRampPlacer.execute();
        localTemperatureGenerator.execute();
        localForestGenerator.execute();
        localFaunaGenerator.execute();
        localFurnitureGenerator.execute();
        localFloorPlacer.execute();
        localItemsGenerator.execute();
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
