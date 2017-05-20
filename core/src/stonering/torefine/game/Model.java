package stonering.torefine.game;

import stonering.menu.worldgen.generators.local.LocalMapGenerator;
import stonering.menu.worldgen.generators.world.WorldGenFactory;
import stonering.torefine.localgen.localmap.Level;
import stonering.torefine.localgen.localmap.LocalMap;
import stonering.torefine.localgen.localmap.MapSnapshot;
import stonering.utils.Position;
import stonering.menu.worldgen.generators.world.GeneratorContainer;
import stonering.menu.worldgen.generators.world.WorldMap;
import stonering.menu.GameModel;
import stonering.menu.GameView;

/**
 * mock model class
 */
public class Model implements GameModel {
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
}
