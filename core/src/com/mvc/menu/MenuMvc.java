package com.mvc.menu;

import com.mvc.MvcContainer;

/**
 * Created by Alexander on 02.04.2017.
 */
public class MenuMvc implements MvcContainer{
	private MenuModel model;
	private MenuView view;
	private MenuController controller;

	public MenuMvc() {
		model = new MenuModel();
		view = new MenuView();
		controller = new MenuController();
		linkComponents();
	}

	private void linkComponents() {
		controller.setModel(model);
		model.setView(view);
		view.setModel(model);
	}

	@Override
	public MenuController getController() {
		return controller;
	}

	@Override
	public MenuView getView() {
		return view;
	}

	@Override
	public MenuModel getModel() {
		return model;
	}
}
