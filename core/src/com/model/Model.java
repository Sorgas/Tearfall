package com.model;

import com.model.generator.local.LocalMapGenerator;
import com.model.generator.world.WorldGenFactory;
import com.model.localmap.Level;
import com.model.localmap.LocalMap;
import com.model.localmap.MapSnapshot;
import com.model.utils.Position;
import com.model.generator.world.generators.GeneratorContainer;
import com.model.generator.world.map_objects.WorldMap;
import com.view.GameView;

/**
 * mock model class
 */
public class Model implements GameModel{
	private LocalMap localMap;
	private LocalMapGenerator localMapGenerator;
	private WorldMap worldMap;
	private GeneratorContainer worldGenerator;

	/**
	 * model constructor. creates flat local with fixed size
	 */
	public Model() {
		WorldGenFactory factory = WorldGenFactory.getInstance();
		localMapGenerator = new LocalMapGenerator();
		//localMapGenerator.createFlatMap(30,30);
		localMap = localMapGenerator.getMap();
		worldGenerator = new GeneratorContainer();
	}

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

	public void makeTurn() {
		
	}

	public WorldMap getWorldMap() {
		return worldMap;
	}

	public void setWorldMap(WorldMap worldMap) {
		this.worldMap = worldMap;
	}

	public GeneratorContainer getWorldGenerator() {
		return worldGenerator;
	}

	@Override
	public void init() {

	}

	public void setView(GameView view) {

	}

	@Override
	public void showFrame() {

	}
}
