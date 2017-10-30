package stonering.generators.localgen;

import stonering.game.core.model.LocalMap;
import stonering.generators.localgen.generators.LocalCaveGenerator;
import stonering.generators.localgen.generators.LocalHeightsGenerator;
import stonering.generators.localgen.generators.LocalRiverGenerator;
import stonering.generators.localgen.generators.LocalStoneLayersGenerator;
import stonering.generators.worldgen.WorldMap;
import stonering.utils.Position;
import stonering.utils.Vector;

/**
 * Created by Alexander on 27.08.2017.
 */
public class LocalGeneratorContainer {
    private LocalGenContainer localGenContainer;

    private LocalHeightsGenerator localHeightsGenerator;
    private LocalStoneLayersGenerator localStoneLayersGenerator;
    private LocalRiverGenerator riverGenerator;
    private LocalCaveGenerator localCaveGenerator;

    private WorldMap world;
    private LocalMap localMap;
    private Position location;
    private LocalGenConfig config;

    public LocalGeneratorContainer(LocalGenConfig config, WorldMap world) {
        this.world = world;
        this.location = config.getLocation();
        this.config = config;
        init();
    }

    public void init() {
        localGenContainer = new LocalGenContainer(config, world);
        localHeightsGenerator = new LocalHeightsGenerator(localGenContainer);
        localStoneLayersGenerator = new LocalStoneLayersGenerator(localGenContainer);
        localCaveGenerator = new LocalCaveGenerator(localGenContainer);
    }

    public void execute() {
        localHeightsGenerator.execute();
        localStoneLayersGenerator.execute();
        localCaveGenerator.execute();
    }

    private float calculateMidElevation(int x, int y) {
        float midElevation = world.getElevation(x, y);
        midElevation += world.getElevation(x + 1, y);
        midElevation += world.getElevation(x, y + 1);
        return (midElevation + world.getElevation(x + 1, y + 1)) / 4;
    }

    private Vector calculateSlope() {
        int[][] area = new int[3][3];
        Vector vector = new Vector(0, 0, 0, 0);
        for (int x = location.getX() - 1; x < location.getX() + 1; x++) {
            for (int y = location.getY() - 1; y < location.getY() + 1; y++) {
                area[x][y] = world.getElevation(x, y) * 8 + 100;
            }
        }
        return vector;
    }

    private boolean validateWorldAndLocation() {
        return world != null && location != null &&
                location.getX() >= 0 && location.getX() < world.getWidth() &&
                location.getY() >= 0 && location.getY() < world.getHeight();
    }

    public LocalMap getLocalMap() {
        return localGenContainer.getLocalMap();
    }

    public void setWorld(WorldMap world) {
        this.world = world;
    }

    public void setLocation(Position location) {
        this.location = location;
    }
}
