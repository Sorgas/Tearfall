package com.mvc.menu.new_game;

import com.TearFall;
import com.badlogic.gdx.Screen;
import com.mvc.GameView;

/**
 * Created by Alexander on 14.04.2017.
 */
public class NewGameMenuView implements GameView, Screen{
	private TearFall game;
	private NewGameMenuController controller;
	private NewGameMenuModel model;

	public NewGameMenuView(TearFall game) {
		this.game = game;
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {

	}

	@Override
	public void resize(int width, int height) {

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

	public void setController(NewGameMenuController controller) {
		this.controller = controller;
	}

	public void setModel(NewGameMenuModel model) {
		this.model = model;
	}
}
