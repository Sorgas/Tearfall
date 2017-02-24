package com.model;

import com.model.generator.local.LocalMapGenerator;
import com.model.localmap.Level;
import com.model.localmap.LocalMap;
import com.model.localmap.MapSnapshot;
import com.model.utils.Position;
import com.model.generator.world.WorldGenerator;
import com.model.generator.world.WorldMap;

/**
 * mock model class
 */
public class FlatworldModel implements GameModel{
	private LocalMap localMap;
	private LocalMapGenerator localMapGenerator;
	private WorldMap worldMap;
	private WorldGenerator worldGenerator;

	/**
	 * model constructor. creates flat local with fixed size
	 */
	public FlatworldModel() {
		localMapGenerator = new LocalMapGenerator();
		localMapGenerator.createFlatMap(30,30);
		localMap = localMapGenerator.getMap();
		worldGenerator = new WorldGenerator();
		worldGenerator.createMap(100,100);
		worldMap = worldGenerator.getMap();
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

	public WorldMap getWorldMap() {
		return worldMap;
	}

	public void setWorldMap(WorldMap worldMap) {
		this.worldMap = worldMap;
	}

	@Override
	public WorldGenerator getWorldGenerator() {
		return worldGenerator;
	}
}
