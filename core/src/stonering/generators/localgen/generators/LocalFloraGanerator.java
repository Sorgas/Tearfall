package stonering.generators.localgen.generators;

import stonering.game.core.model.LocalMap;
import stonering.generators.localgen.LocalGenConfig;
import stonering.generators.localgen.LocalGenContainer;

public class LocalFloraGanerator {
    private LocalGenContainer container;
    private LocalGenConfig config;
    private int localAreaSize;
    private LocalMap localMap;

    public LocalFloraGanerator(LocalGenContainer container) {
        this.container = container;
        this.config = container.getConfig();
        this.localAreaSize = config.getAreaSize();
        this.localMap = container.getLocalMap();
    }

    public void generateFlora() {

    }

    private void querySpecies() {

    }
}
