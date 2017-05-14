package com;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.menu.main.MainMenuMvc;
import com.menu.new_game.prepare_expedition.PrepareExpeditionMenuMvc;
import com.menu.new_game.select_location.SelectLocationMenuMvc;
import com.menu.new_game.select_world.SelectWorldMenuMvc;
import com.menu.worldgen.WorldGenMvc;
import com.menu.worldgen.generators.world.WorldMap;
import com.utils.Position;

/**
 * Created by Alexander on 08.04.2017.
 */
public class TearFall extends Game {
	private MainMenuMvc mainMenuMvc;
	private WorldGenMvc worldGenMvc;
	private SelectWorldMenuMvc selectWorldMenuMvc;
	private SelectLocationMenuMvc selectLocationMenuMvc;
	private PrepareExpeditionMenuMvc prepareExpeditionMenuMvc;
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

	public void switchWorldsSelectMenu() {
		if(selectWorldMenuMvc == null) selectWorldMenuMvc = new SelectWorldMenuMvc(this);
		setScreen(selectWorldMenuMvc.getView());
	}

	public void switchLocationSelectMenu(WorldMap world) {
		if(selectLocationMenuMvc == null) selectLocationMenuMvc = new SelectLocationMenuMvc(this);
		selectLocationMenuMvc.getModel().setWorld(world);
		setScreen(selectLocationMenuMvc.getView());
	}

	public void switchPrepareExpeditionMenu(WorldMap world, Position location) {
		if(prepareExpeditionMenuMvc == null) prepareExpeditionMenuMvc = new PrepareExpeditionMenuMvc(this);
		prepareExpeditionMenuMvc.getModel().setWorld(world);
		prepareExpeditionMenuMvc.getModel().setLocation(location);
		setScreen(prepareExpeditionMenuMvc.getView());
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
