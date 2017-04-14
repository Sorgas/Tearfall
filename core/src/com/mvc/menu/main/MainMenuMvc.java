package com.mvc.menu.main;

import com.TearFall;
import com.mvc.MvcContainer;

/**
 * Created by Alexander on 02.04.2017.
 */
public class MainMenuMvc implements MvcContainer{
	private MainMenuModel model;
	private MainMenuView view;
	private MainMenuController controller;

	public MainMenuMvc(TearFall game) {
		model = new MainMenuModel(game);
		view = new MainMenuView();
		controller = new MainMenuController();
		linkComponents();
	}

	private void linkComponents() {
		controller.setModel(model);
		model.setView(view);
		view.setModel(model);
	}

	@Override
	public MainMenuController getController() {
		return controller;
	}

	@Override
	public MainMenuView getView() {
		return view;
	}

	@Override
	public MainMenuModel getModel() {
		return model;
	}
}
