package stonering.generators.localgen;

import stonering.game.core.model.LocalMap;
import stonering.generators.localgen.generators.*;
import stonering.generators.worldgen.WorldMap;
import stonering.global.utils.Position;

/**
 * Created by Alexander on 27.08.2017.
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
        localForestGenerator = new LocalForestGenerator(localGenContainer);
        localFaunaGenerator = new LocalFaunaGenerator(localGenContainer);
    }

    public void execute() {
        localHeightsGenerator.execute();
        localStoneLayersGenerator.execute();
        localCaveGenerator.execute();
        localRampPlacer.execute();
        localForestGenerator.execute();
        localFaunaGenerator.execute();
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
