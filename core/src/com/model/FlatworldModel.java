package com.model;

import com.model.generator.LocalMapGenerator;
import com.model.localmap.Level;
import com.model.localmap.LocalMap;
import com.model.localmap.MapSnapshot;
import com.model.localmap.Position;

/**
 * mock model class
 */
public class FlatworldModel implements GameModel{
	private LocalMap localMap;
	private LocalMapGenerator localMapGenerator;

	/**
	 * model constructor. creates flat local with fixed size
	 */
	public FlatworldModel() {
		this.localMapGenerator = new LocalMapGenerator();
		localMapGenerator.createFlatMap(30,30);
		localMap = localMapGenerator.getMap();
	}
	
	@Override
	public MapSnapshot prepareSnapshot(Position camera) {
		MapSnapshot snapshot = new MapSnapshot(localMap.getxSize(), localMap.getySize());
		Level level;
		for (int i = camera.getZ() - 10; i < camera.getZ(); i++) {
			level = localMap.getLevel(i);
			snapshot.addLevel(level);
		}
		snapshot.setxSize(localMap.getxSize());
		snapshot.setySize(localMap.getySize());
		return snapshot;
	}

	@Override
	public void makeTurn() {
		
	}
}
