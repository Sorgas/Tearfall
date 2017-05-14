package com.menu.new_game.prepare_expedition;

import com.TearFall;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.torefine.GameModel;
import com.menu.worldgen.generators.world.WorldMap;
import com.menu.ui_components.WorldListItem;
import com.utils.Position;

import java.io.File;

/**
 * Created by Alexander on 14.04.2017.
 */
public class PrepareExpeditionMenuModel implements GameModel{
	private TearFall game;
	private PrepareExpeditionMenuView view;
	private Stage stage;
	private Table table;


	private WorldMap world;
	private Position location;

	public PrepareExpeditionMenuModel(TearFall game) {
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

	public void setView(PrepareExpeditionMenuView view) {
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

	public void setLocation(Position location) {
		this.location = location;
	}
}