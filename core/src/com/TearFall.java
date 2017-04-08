package com;

import com.badlogic.gdx.Game;
import com.mvc.menu.MenuScreen;

/**
 * Created by Alexander on 08.04.2017.
 */
public class TearFall extends Game {
	@Override
	public void create() {
		setScreen(new MenuScreen());
	}
}
