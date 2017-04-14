package com.mvc.worldgen;

import com.mvc.GameController;

/**
 * Created by Alexander on 08.03.2017.
 */
public class WorldGenController implements GameController {
	private WorldGenModel model;

	public void setModel(WorldGenModel model) {
		this.model = model;
	}

	public void generateWorld() {
		model.init();
	}
}