package com.mvc.worldgen.generators.world.generators.drainage;

import com.mvc.worldgen.generators.world.generators.AbstractGenerator;
import com.mvc.worldgen.generators.world.WorldGenContainer;

/**
 * Created by Alexander on 31.03.2017.
 */
public class RainfallGenerator extends AbstractGenerator {

	public RainfallGenerator(WorldGenContainer container) {
		super(container);
	}

	@Override
	public boolean execute() {
		return false;
	}
}
