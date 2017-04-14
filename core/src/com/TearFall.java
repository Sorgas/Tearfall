package com;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mvc.menu.main.MainMenuMvc;
import com.mvc.menu.new_game.NewGameMenuMvc;
import com.mvc.worldgen.WorldGenMvc;

/**
 * Created by Alexander on 08.04.2017.
 */
public class TearFall extends Game {
	private MainMenuMvc mainMenuMvc;
	private WorldGenMvc worldGenMvc;
	private NewGameMenuMvc newGameMenuMvc;
	private BitmapFont font;
	private Skin skin;

	@Override
	public void create() {
		createFont();
		createSkin();
		switchMainMenu();
	}

	public void switchMainMenu() {
		if(mainMenuMvc == null) mainMenuMvc = new MainMenuMvc(this);
		setScreen(mainMenuMvc.getView());
	}

	public void switchWorldGenMenu() {
		if(worldGenMvc == null) worldGenMvc = new WorldGenMvc(this);
		setScreen(worldGenMvc.getView());
	}

	public void switchNewGameMenu() {
		if(newGameMenuMvc == null) newGameMenuMvc = new NewGameMenuMvc(this);
		setScreen(newGameMenuMvc.getView());
	}

	private void createFont() {
		font = new BitmapFont();
		font.setColor(0.2f, 0.2f, 0.2f, 1);
	}

	private void createSkin() {
		TextureAtlas atlas = new TextureAtlas(new FileHandle("ui_skin/uiskin.atlas"));
		skin = new Skin(new FileHandle("ui_skin/uiskin.json"), atlas);
	}

	public BitmapFont getFont() {
		return font;
	}

	public Skin getSkin() {
		return skin;
	}


}
