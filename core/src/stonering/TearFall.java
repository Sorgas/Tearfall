package stonering;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import stonering.entity.world.World;
import stonering.game.core.GameMvc;
import stonering.generators.localgen.LocalGenContainer;
import stonering.menu.main.MainScreen;
import stonering.menu.new_game.LocalGenerationScreen;
import stonering.menu.new_game.PrepareExpeditionMenu;
import stonering.menu.new_game.SelectLocationMenu;
import stonering.menu.new_game.SelectWorldScreen;
import stonering.global.utils.Position;
import stonering.menu.worldgen.WorldGenScreen;

/**
 * Game object. Container of screens.
 *
 * @author Alexander Kuzyakov on 08.04.2017.
 */
public class TearFall extends Game {
    private MainScreen mainScreen;
    private WorldGenScreen worldGenScreen;
    private SelectWorldScreen selectWorldScreen;
    private SelectLocationMenu selectLocationMenu;
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
        if (mainScreen == null) mainScreen = new MainScreen(this);
        setScreen(mainScreen);
    }

    public void switchWorldGenMenu() {
        if (worldGenScreen == null) worldGenScreen = new WorldGenScreen(this);
        setScreen(worldGenScreen);
    }

    public void switchWorldsSelectMenu() {
        if (selectWorldScreen == null) selectWorldScreen = new SelectWorldScreen(this);
        setScreen(selectWorldScreen);
    }

    public void switchLocationSelectMenu(World world) {
        if (selectLocationMenu == null) selectLocationMenu = new SelectLocationMenu(this);
        selectLocationMenu.setWorld(world);
        setScreen(selectLocationMenu);
    }

    public void switchPrepareExpeditionMenu(World world, Position location) {
        if (prepareExpeditionMenuMvc == null) prepareExpeditionMenuMvc = new PrepareExpeditionMenu(this);
        prepareExpeditionMenuMvc.setWorld(world);
        prepareExpeditionMenuMvc.setLocation(location);
        setScreen(prepareExpeditionMenuMvc);
    }

    public void switchToLocalGen(World world, Position location) {
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
