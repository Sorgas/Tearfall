package stonering.generators.localgen;

import stonering.game.core.model.LocalMap;
import stonering.generators.worldgen.WorldMap;

/**
 * Created by Alexander on 29.08.2017.
 */
public class LocalGenContainer {
    private LocalGenConfig config;
    private WorldMap worldMap;
    private LocalMap map;


    public LocalGenContainer(LocalGenConfig config, WorldMap worldMap) {
        this.config = config;
        this.worldMap = worldMap;
    }

    public LocalGenConfig getConfig() {
        return config;
    }

    public void setConfig(LocalGenConfig config) {
        this.config = config;
    }

    public WorldMap getWorldMap() {
        return worldMap;
    }
}
