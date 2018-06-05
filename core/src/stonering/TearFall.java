package stonering;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import stonering.game.core.GameMvc;
import stonering.generators.localgen.LocalGenContainer;
import stonering.menu.main.MainMenu;
import stonering.menu.new_game.local_generation.LocalGenerationScreen;
import stonering.menu.new_game.prepare_expedition.PrepareExpeditionMenu;
import stonering.menu.new_game.select_location.SelectLocationMenuMvc;
import stonering.menu.new_game.select_world.SelectWorldMenu;
import stonering.menu.worldgen.WorldGenMvc;
import stonering.generators.worldgen.WorldMap;
import stonering.global.utils.Position;

/**
 * s
 * Created by Alexander on 08.04.2017.
 */
public class TearFall extends Game {
    private MainMenu mainMenu;
    private WorldGenMvc worldGenMvc;
    private SelectWorldMenu selectWorldMenu;
    private SelectLocationMenuMvc selectLocationMenuMvc;
    private PrepareExpeditionMenu prepareExpeditionMenuMvc;
    private LocalGenerationScreen localGenerationScreen;
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
        setScreen(mainMenu != null ? mainMenu : new MainMenu(this));
    }

    public void switchWorldGenMenu() {
        if (worldGenMvc == null) worldGenMvc = new WorldGenMvc(this);
        setScreen(worldGenMvc.getView());
    }

    public void switchWorldsSelectMenu() {
        setScreen(selectWorldMenu != null ? selectWorldMenu : new SelectWorldMenu(this));
    }

    public void switchLocationSelectMenu(WorldMap world) {
        if (selectLocationMenuMvc == null) selectLocationMenuMvc = new SelectLocationMenuMvc(this);
        selectLocationMenuMvc.getModel().setWorld(world);
        setScreen(selectLocationMenuMvc.getView());
    }

    public void switchPrepareExpeditionMenu(WorldMap world, Position location) {
        if (prepareExpeditionMenuMvc == null) prepareExpeditionMenuMvc = new PrepareExpeditionMenu(this);
        prepareExpeditionMenuMvc.setWorld(world);
        prepareExpeditionMenuMvc.setLocation(location);
        setScreen(prepareExpeditionMenuMvc);
    }

    public void switchToLocalGen(WorldMap world, Position location) {
        if (localGenerationScreen == null) localGenerationScreen = new LocalGenerationScreen(this);
        localGenerationScreen.setWorld(world);
        localGenerationScreen.setLocation(location);
        setScreen(localGenerationScreen);
    }

    public void switchToGame(LocalGenContainer container) {
        gameMvc = new GameMvc(container);
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
