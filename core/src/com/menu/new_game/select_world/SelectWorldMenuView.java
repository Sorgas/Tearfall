package com.menu.new_game.select_world;

import com.TearFall;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.torefine.GameView;
import com.menu.utils.WorldSaver;
import com.menu.worldgen.generators.world.WorldMap;
import com.menu.ui_components.MiniMap;
import com.menu.ui_components.WorldListItem;

/**
 * Created by Alexander on 14.04.2017.
 */
public class SelectWorldMenuView implements GameView, Screen {
	private TearFall game;
	private SelectWorldMenuController controller;
	private SelectWorldMenuModel model;
	private Stage stage;
	private List<WorldListItem> worldList;
	private MiniMap minimap;


	public SelectWorldMenuView(TearFall game) {
		this.game = game;
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		stage = new Stage();
		init();
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
		model.checkInput();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.dispose();
		stage = new Stage();
		init();
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}

	private void init() {
		stage.setDebugAll(true);
		Table rootTable = new Table();
		rootTable.setFillParent(true);
		rootTable.defaults().fill().expandY().space(10);
		rootTable.pad(10).align(Align.bottomLeft);
		rootTable.add(createMenuTable());
		rootTable.add(createMinimap()).expandX();
		stage.addActor(rootTable);
	}

	private Table createMinimap() {
		minimap = new MiniMap(new Texture("map_tiles.png"));
		minimap.setMap(new WorldSaver().loadWorld(worldList.getSelected().getTitle()));
		return minimap;
	}

	private Table createMenuTable() {
		Table menuTable = new Table();
		menuTable.defaults().prefHeight(30).prefWidth(300).padBottom(10).minWidth(300);
		menuTable.align(Align.bottomLeft);

		menuTable.add(new Label("Select world:", game.getSkin()));
		menuTable.row();

		worldList = new List<>(game.getSkin());
		worldList.setItems(model.getWorldListItems());
		worldList.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				WorldMap world = new WorldSaver().loadWorld(((List<WorldListItem>) actor).getSelected().getTitle());
				model.setWorld(world);
				minimap.setMap(world);
			}
		});
		menuTable.add(worldList);
		menuTable.row();

		menuTable.add().expandY();
		menuTable.row();

		TextButton proceedButton = new TextButton("Proceed", game.getSkin());
		proceedButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.switchLocationSelectMenu(model.getWorld());
			}
		});
		menuTable.add(proceedButton);
		menuTable.row();

		TextButton backButton = new TextButton("Back", game.getSkin());
		backButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.switchMainMenu();
			}
		});
		menuTable.add(backButton).colspan(2).pad(0);

		return menuTable;
	}

	public void setController(SelectWorldMenuController controller) {
		this.controller = controller;
	}

	public void setModel(SelectWorldMenuModel model) {
		this.model = model;
	}
}
