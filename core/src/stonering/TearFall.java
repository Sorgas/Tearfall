package stonering;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import stonering.game.core.GameMvc;
import stonering.game.core.model.LocalMap;
import stonering.menu.main.MainMenuMvc;
import stonering.menu.new_game.local_generation.LocalGenerationMvc;
import stonering.menu.new_game.prepare_expedition.PrepareExpeditionMenuMvc;
import stonering.menu.new_game.select_location.SelectLocationMenuMvc;
import stonering.menu.new_game.select_world.SelectWorldMenuMvc;
import stonering.menu.worldgen.WorldGenMvc;
import stonering.generators.worldgen.WorldMap;
import stonering.global.utils.Position;

/**s
 * Created by Alexander on 08.04.2017.
 */
public class TearFall extends Game {
	private MainMenuMvc mainMenuMvc;
	private WorldGenMvc worldGenMvc;
	private SelectWorldMenuMvc selectWorldMenuMvc;
	private SelectLocationMenuMvc selectLocationMenuMvc;
	private PrepareExpeditionMenuMvc prepareExpeditionMenuMvc;
	private LocalGenerationMvc localGenerationMvc;
	private GameMvc gameMvc;

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

	public void switchToLocalGen(WorldMap world, Position location) {
		if(localGenerationMvc == null) localGenerationMvc = new LocalGenerationMvc(this);
		localGenerationMvc.getModel().setWorld(world);
		localGenerationMvc.getModel().setLocation(location);
		setScreen(localGenerationMvc.getView());
	}

	public void switchToGame(LocalMap localMap) {
		gameMvc = new GameMvc(localMap);
		setScreen(gameMvc.getView());
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
