package stonering.menu.worldgen;

import stonering.TearFall;
import stonering.menu.GameController;
import stonering.menu.utils.WorldSaver;

import java.util.Random;

/**
 * Created by Alexander on 08.03.2017.
 */
public class WorldGenController implements GameController {
	private TearFall game;
	private WorldGenModel model;
	private Random random;

	public WorldGenController(TearFall game) {
		this.game = game;
		random = new Random();
	}

	public void randomizeSeed() {
		model.setSeed(random.nextLong());
	}

	public void setModel(WorldGenModel model) {
		this.model = model;
	}

	public void generateWorld() {
		model.generateWorld();
	}

	public void saveMap() {
		new WorldSaver().saveWorld(model.getMap());
		game.switchMainMenu();
	}


}