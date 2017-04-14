package com.mvc.worldgen;

import com.TearFall;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.mvc.GameModel;
import com.mvc.worldgen.generators.world.GeneratorContainer;
import com.mvc.worldgen.generators.world.WorldGenConfig;
import com.mvc.worldgen.generators.world.WorldGenContainer;
import com.mvc.worldgen.generators.world.WorldGenFactory;
import com.mvc.worldgen.generators.world.WorldMap;

import java.io.*;
import java.util.Random;

/**
 * Created by Alexander on 06.03.2017.
 */
public class WorldGenModel implements GameModel {
	private WorldGenContainer worldGenContainer;
	private GeneratorContainer generatorContainer;
	private WorldMap map;

	private WorldGenView view;
	private TearFall game;
	private Stage stage;
	private Table menuTable;
	private Table settingsTable;

	private int worldSize = 100;

	private TextField seedField;

	private long seed;
	private Random random;

	public WorldGenModel(TearFall game) {
		this.game = game;
		random = new Random();
		seed = random.nextLong();
		createMenuTable();
		createSettingsTable();
		init();
	}

	private void generateWorld() {
		WorldGenFactory factory = WorldGenFactory.getInstance();
		WorldGenConfig config = new WorldGenConfig(1234, worldSize, worldSize);
		factory.initMapContainer(config);
		generatorContainer = factory.getGeneratorContainer();
		generatorContainer.runContainer();
		worldGenContainer = generatorContainer.getWorldGenContainer();
		map = worldGenContainer.getMap();
	}

	@Override
	public void init() {
		stage = new Stage();
//		stage.setDebugAll(true);
		stage.addActor(settingsTable);
		stage.addActor(menuTable);
	}

	private void createSettingsTable() {
		settingsTable = new Table();
		settingsTable.defaults().height(30).pad(0, 0, 0, 10);
		settingsTable.pad(100, 10, 10, 10);
		settingsTable.left().top();
		settingsTable.setFillParent(true);

		settingsTable.add(new Label("Seed: ", game.getSkin())).colspan(2).align(Align.left);
		settingsTable.row();
		seedField = new TextField(new Long(seed).toString(), game.getSkin());
		settingsTable.add(seedField).width(260);

		TextButton randButton = new TextButton("R", game.getSkin());
		randButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				seed = random.nextLong();
				seedField.setText(new Long(seed).toString());
			}
		});
		settingsTable.add(randButton).width(30);
		settingsTable.row();

		settingsTable.add(new Label("World size: ", game.getSkin())).colspan(2).align(Align.left).pad(10, 0, 0, 0);
		settingsTable.row();

		Slider worldSizeSlider = new Slider(100, 500, 100, false, game.getSkin());
		Label worldSizeLabel = new Label("100", game.getSkin());

		worldSizeSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				worldSize = Math.round(((Slider) actor).getValue());
				worldSizeLabel.setText(new Integer(worldSize).toString());
			}
		});

		settingsTable.add(worldSizeSlider).width(260);
		settingsTable.add(worldSizeLabel);
	}

	private void createMenuTable() {
		menuTable = new Table();
		menuTable.defaults().height(30).width(300).pad(10, 0, 0, 0);
		menuTable.pad(10);
		menuTable.left().bottom();

		TextButton generateButton = new TextButton("Generate", game.getSkin());
		generateButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				generateWorld();
			}
		});
		menuTable.add(generateButton);
		menuTable.row();

		TextButton saveButton = new TextButton("Save", game.getSkin());
		saveButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				saveMap();
			}
		});
		menuTable.add(saveButton);
		menuTable.row();

		TextButton backButton = new TextButton("Back", game.getSkin());
		backButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.switchMainMenu();
			}
		});
		menuTable.add(backButton);
	}

	private void saveMap() {
		try {
			File file = new File("saves/" + "1.world");
			file.getParentFile().mkdirs();
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file.getPath());
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(map);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setView(WorldGenView view) {
		this.view = view;
	}

	public WorldGenContainer getWorldGenContainer() {
		return worldGenContainer;
	}

	public WorldMap getMap() {
		return map;
	}

	public Stage getStage() {
		return stage;
	}

	public void reset() {
		stage.dispose();
		init();
	}
}