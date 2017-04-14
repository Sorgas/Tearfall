package com.mvc.menu.new_game;

import com.TearFall;
import com.mvc.GameController;

/**
 * Created by Alexander on 14.04.2017.
 */
public class NewGameMenuController implements GameController {
	private NewGameMenuModel model;

	public NewGameMenuController(TearFall game) {
	}

	public void setModel(NewGameMenuModel model) {
		this.model = model;
	}
}
