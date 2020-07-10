package stonering.generators.worldgen.generators;

import stonering.generators.worldgen.WorldGenContainer;

/**
 * Abstract world generator. Has container of intermediate results.
 * 
 * @author Alexander Kuzyakov on 26.03.2017.
 */
public abstract class WorldGenerator {
	protected final WorldGenContainer container;

	public WorldGenerator(WorldGenContainer container) {
		this.container = container;
	}

	public abstract boolean execute();
}
