package stonering.generators.localgen.generators;

import stonering.generators.localgen.LocalGenConfig;
import stonering.generators.localgen.LocalGenContainer;

public abstract class LocalGenerator {
    protected LocalGenContainer container;
    protected LocalGenConfig config;

    public LocalGenerator(LocalGenContainer container) {
        this.container = container;
        config = container.config;
    }

    public abstract void execute();
}
