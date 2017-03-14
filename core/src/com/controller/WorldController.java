package com.controller;

import com.model.WorldGenModel;

/**
 * Created by Alexander on 08.03.2017.
 */
public class WorldController implements GameController{
	private WorldGenModel model;

	@Override
	public void init() {

	}

	public void setModel(WorldGenModel model) {
		this.model = model;
	}

	@Override
	public void handleButtonPress() {

	}

	@Override
	public void showFrame() {
		model.showFrame();
	}

	public void generateWorld() {
		model.init();
	}
}