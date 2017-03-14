package com.MVCcontainer;

import com.controller.WorldController;
import com.model.GameModel;
import com.model.WorldGenModel;
import com.view.WorldView;

/**
 * Created by Alexander on 08.03.2017.
 */
public class WorldGenMvc implements MvcContainer{
	private WorldGenModel model;
	private WorldView view;
	private WorldController controller;

	public WorldGenMvc() {
		this.model = new WorldGenModel();
		this.view = new WorldView();
		this.controller = new WorldController();
		linkComponents();
	}

	private void linkComponents() {
		controller.setModel(model);
		model.setView(view);
		view.setModel(model);
	}

	@Override
	public WorldView getView() {
		return view;
	}

	@Override
	public GameModel getModel() {
		return null;
	}

	@Override
	public WorldController getController() {
		return controller;
	}
}