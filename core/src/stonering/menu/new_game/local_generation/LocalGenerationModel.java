package stonering.menu.new_game.local_generation;

import stonering.generators.localgen.LocalGenConfig;
import stonering.generators.localgen.LocalGenContainer;
import stonering.generators.localgen.LocalGeneratorContainer;
import stonering.game.core.model.LocalMap;
import stonering.generators.worldgen.WorldMap;
import stonering.global.utils.Position;

/**
 * Created by Alexander on 01.06.2017.
 */
public class LocalGenerationModel{
    private LocalGeneratorContainer localGeneratorContainer;
    private WorldMap world;
    private Position location;
    private LocalMap localMap;

    public void setWorld(WorldMap world) {
        this.world = world;
    }

    public void setLocation(Position location) {
        this.location = location;
    }

    public void generateLocal() {
        LocalGenConfig config = new LocalGenConfig();
        config.setLocation(location);
        localGeneratorContainer = new LocalGeneratorContainer(config, world);
        localGeneratorContainer.execute();
        localMap = localGeneratorContainer.getLocalMap();
    }

    public LocalMap getLocalMap() {
        return localMap;
    }

    public LocalGeneratorContainer getLocalGeneratorContainer() {
        return localGeneratorContainer;
    }
}
