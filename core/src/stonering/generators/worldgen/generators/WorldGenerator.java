package stonering.generators.worldgen.generators;

import stonering.generators.worldgen.WorldGenConfig;
import stonering.generators.worldgen.WorldGenContainer;

/**
 * Abstract world generator. Has container of intermediate results.
 *
 * @author Alexander Kuzyakov on 26.03.2017.
 */
public abstract class WorldGenerator {
    protected WorldGenContainer container;
    protected WorldGenConfig config;

    public void execute(WorldGenContainer container) {
        this.container = container;
        this.config = container.config;
        set(container);
        run();
    }

    public abstract void set(WorldGenContainer container);

    public abstract void run();
}
