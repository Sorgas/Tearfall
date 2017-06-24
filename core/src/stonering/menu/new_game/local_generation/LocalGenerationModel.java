package stonering.menu.new_game.local_generation;

import stonering.generators.localgen.LocalMapGenerator;
import stonering.game.core.LocalMap;
import stonering.generators.worldgen.WorldMap;
import stonering.utils.Position;

/**
 * Created by Alexander on 01.06.2017.
 */
public class LocalGenerationModel {
    private LocalMapGenerator localMapGenerator;

    private WorldMap world;
    private Position location;

    private LocalMap localMap;

    public LocalGenerationModel() {
        localMapGenerator = new LocalMapGenerator();
    }

    public void setWorld(WorldMap world) {
        this.world = world;
    }

    public void setLocation(Position location) {
        this.location = location;
    }

    public void generateLocal() {
        localMapGenerator.setWorld(world);
        localMapGenerator.setLocation(location);
        localMapGenerator.execute();
        localMap = localMapGenerator.getLocalMap();
    }

    public LocalMap getLocalMap() {
        return localMap;
    }
}
