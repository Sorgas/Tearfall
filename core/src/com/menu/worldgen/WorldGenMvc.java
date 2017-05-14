package com.menu.worldgen;

import com.TearFall;
import com.torefine.MvcContainer;

/**
 * Created by Alexander on 08.03.2017.
 */
public class WorldGenMvc implements MvcContainer {
	private WorldGenModel model;
	private WorldGenView view;
	private WorldGenController controller;

	public WorldGenMvc(TearFall game) {
		this.model = new WorldGenModel(game);
		this.view = new WorldGenView(game);
		this.controller = new WorldGenController(game);
		linkComponents();
	}

	private void linkComponents() {
		controller.setModel(model);
		model.setView(view);
		view.setModel(model);
		view.setController(controller);
	}

	@Override
	public WorldGenView getView() {
		return view;
	}

	@Override
	public WorldGenModel getModel() {
		return model;
	}

	@Override
	public WorldGenController getController() {
		return controller;
	}
}