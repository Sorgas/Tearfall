package stonering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import stonering.entity.world.World;
import stonering.game.GameMvc;
import stonering.screen.MainMenuScreen;
import stonering.screen.LocalGenerationScreen;
import stonering.screen.PrepareExpeditionMenu;
import stonering.screen.SelectLocationMenu;
import stonering.screen.SelectWorldScreen;
import stonering.util.geometry.Position;
import stonering.screen.WorldGenScreen;
import stonering.widget.GameWithCustomCursor;

/**
 * Game object. Container of screens.
 *
 * @author Alexander Kuzyakov on 08.04.2017.
 */
public class TearFall extends GameWithCustomCursor {
    private MainMenuScreen mainMenuScreen;
    private WorldGenScreen worldGenScreen;
    private SelectWorldScreen selectWorldScreen;
    private SelectLocationMenu selectLocationMenu;
    private PrepareExpeditionMenu prepareExpeditionMenuMvc;
    private LocalGenerationScreen localGenerationScreen;
    private Texture cursor;

    @Override
    public void create() {
        super.create();
        switchMainMenu();
    }

    public void switchMainMenu() {
        if (mainMenuScreen == null) mainMenuScreen = new MainMenuScreen(this);
        setScreen(mainMenuScreen);
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
        if (localGenerationScreen == null) localGenerationScreen = new LocalGenerationScreen(this, world, location);
        setScreen(localGenerationScreen);
    }

    public void switchToGame() {
        setScreen(GameMvc.view());
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
        super.render();
    }
}
