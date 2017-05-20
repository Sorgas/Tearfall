package stonering.menu.worldgen.generators.world.generators;

import stonering.menu.worldgen.generators.world.WorldGenContainer;

/**
 * Created by Alexander on 26.03.2017.
 */
public abstract class AbstractGenerator {
	protected final WorldGenContainer container;

	public AbstractGenerator(WorldGenContainer container) {
		this.container = container;
	}

	public abstract boolean execute();
}
