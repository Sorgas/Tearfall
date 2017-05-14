package com.menu.new_game.select_world;

import com.TearFall;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.torefine.GameModel;
import com.menu.worldgen.generators.world.WorldMap;
import com.menu.ui_components.WorldListItem;

import java.io.File;

/**
 * Created by Alexander on 14.04.2017.
 */
public class SelectWorldMenuModel implements GameModel{
	private TearFall game;
	private SelectWorldMenuView view;
	private WorldMap world;
	private Stage stage;
	private Table table;

	public SelectWorldMenuModel(TearFall game) {
		this.game = game;
		init();
	}

	@Override
	public void init() {
	}

	public Array<WorldListItem> getWorldListItems() {
		File root = new File("saves");
		Array<WorldListItem> list = new Array<>();
		for (File file : root.listFiles()) {
			list.add(new WorldListItem(file.getName(), file));
		}
		return list;
	}

	public void checkInput() {

	}

	public void setView(SelectWorldMenuView view) {
		this.view = view;
	}

	public Stage getStage() {
		return stage;
	}

	public void setWorld(WorldMap world) {
		this.world = world;
	}

	public WorldMap getWorld() {
		return world;
	}
}