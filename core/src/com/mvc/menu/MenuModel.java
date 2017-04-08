package com.mvc.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.mvc.GameModel;

/**
 * Created by Alexander on 02.04.2017.
 */
public class MenuModel implements GameModel {
	private MenuView view;

	@Override
	public void init() {
		Button button = new Button();
	}

	@Override
	public void showFrame() {
		view.showFrame();
	}

	public void setView(MenuView view) {
		this.view = view;
	}
}
