package stonering.generators.localgen.generators;

import stonering.generators.localgen.LocalGenContainer;

public abstract class LocalAbstractGenerator {
    protected LocalGenContainer container;

    public LocalAbstractGenerator(LocalGenContainer container) {
        this.container = container;
    }

    public abstract void execute();
}
