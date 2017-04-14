package com.mvc.menu.new_game;

import com.TearFall;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mvc.GameModel;

/**
 * Created by Alexander on 14.04.2017.
 */
public class NewGameMenuModel implements GameModel{
	private TearFall game;
	private NewGameMenuView view;
	private Stage stage;

	public NewGameMenuModel(TearFall game) {
		this.game = game;
		init();
	}

	@Override
	public void init() {

	}

	public void setView(NewGameMenuView view) {
		this.view = view;
	}

	public Stage getStage() {
		return stage;
	}
}