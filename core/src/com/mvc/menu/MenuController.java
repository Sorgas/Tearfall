package com.mvc.menu;

import com.mvc.GameController;

/**
 * Created by Alexander on 02.04.2017.
 */
public class MenuController implements GameController {
	private MenuModel model;

	@Override
	public void init() {

	}

	@Override
	public void handleButtonPress() {

	}

	@Override
	public void showFrame() {
		model.showFrame();
	}

	public void setModel(MenuModel model) {
		this.model = model;
	}
}
