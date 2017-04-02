package com.mvc.worldgen;

import com.mvc.GameModel;
import com.mvc.MvcContainer;

/**
 * Created by Alexander on 08.03.2017.
 */
public class WorldGenMvc implements MvcContainer {
	private WorldGenModel model;
	private WorldGenView view;
	private WorldGenController controller;

	public WorldGenMvc() {
		this.model = new WorldGenModel();
		this.view = new WorldGenView();
		this.controller = new WorldGenController();
		linkComponents();
	}

	private void linkComponents() {
		controller.setModel(model);
		model.setView(view);
		view.setModel(model);
	}

	@Override
	public WorldGenView getView() {
		return view;
	}

	@Override
	public GameModel getModel() {
		return null;
	}

	@Override
	public WorldGenController getController() {
		return controller;
	}
}