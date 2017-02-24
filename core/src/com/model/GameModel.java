package com.model;

import com.model.generator.world.WorldGenerator;
import com.model.localmap.MapSnapshot;
import com.model.utils.Position;
import com.model.generator.world.WorldMap;

public interface GameModel {
	/**
	 * creates Snapshot object of model !COPING! given z-level
	 * @param camera
	 * @return Snapshot
	 */
    MapSnapshot prepareSnapshot(Position camera);

	void makeTurn();

	WorldMap getWorldMap();

	public WorldGenerator getWorldGenerator();
}
