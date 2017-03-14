package com.model.generator.world.generators.drainage;

import com.model.generator.world.map_objects.WorldGenContainer;
import com.model.generator.world.map_objects.WorldMap;

import java.util.Random;

/**
 * Created by Alexander on 12.03.2017.
 */
public class OceanFiller {
	private Random random;
	private int width;
	private int height;
	private WorldGenContainer container;
	private int seaLevel;

	public OceanFiller(WorldGenContainer container) {
		this.container = container;
		this.random = container.getConfig().getRandom();
		this.width = container.getConfig().getWidth();
		this.height = container.getConfig().getHeight();
		seaLevel = container.getConfig().getSeaLevel();
	}

	public void execute() {
		WorldMap map = container.getMap();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if(map.getCell(x,y).getElevation() < seaLevel) {
					map.getCell(x,y).setOcean(true);
				}
			}
		}
	}
}
