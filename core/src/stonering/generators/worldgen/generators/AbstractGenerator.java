package stonering.generators.worldgen.generators;

import stonering.generators.worldgen.WorldGenContainer;

/**
 * @author Alexander Kuzyakov on 26.03.2017.
 */
public abstract class AbstractGenerator {
	protected final WorldGenContainer container;

	public AbstractGenerator(WorldGenContainer container) {
		this.container = container;
	}

	public abstract boolean execute();
}
