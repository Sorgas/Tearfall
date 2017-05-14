package com.menu.new_game.select_world;

import com.TearFall;
import com.torefine.GameController;

/**
 * Created by Alexander on 14.04.2017.
 */
public class SelectWorldMenuController implements GameController {
	private SelectWorldMenuModel model;

	public SelectWorldMenuController(TearFall game) {
	}

	public void setModel(SelectWorldMenuModel model) {
		this.model = model;
	}
}
