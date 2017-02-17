package com.model;

import com.model.localmap.MapSnapshot;
import com.model.localmap.Position;

public interface GameModel {
	/**
	 * creates Snapshot object of model !COPING! given z-level
	 * @param camera
	 * @return Snapshot
	 */
    MapSnapshot prepareSnapshot(Position camera);

	void makeTurn();
}
