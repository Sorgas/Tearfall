package com.mvc.menu.new_game;

import com.TearFall;
import com.mvc.MvcContainer;

/**
 * Created by Alexander on 14.04.2017.
 */
public class NewGameMenuMvc implements MvcContainer {
	private NewGameMenuModel model;
	private NewGameMenuView view;
	private NewGameMenuController controller;

	public NewGameMenuMvc(TearFall game) {
		model = new NewGameMenuModel(game);
		view = new NewGameMenuView(game);
		controller = new NewGameMenuController(game);
	}

	private void linkComponents() {
		controller.setModel(model);
		model.setView(view);
		view.setModel(model);
	}

	@Override
	public NewGameMenuController getController() {
		return controller;
	}

	@Override
	public NewGameMenuView getView() {
		return view;
	}

	@Override
	public NewGameMenuModel getModel() {
		return model;
	}
}
