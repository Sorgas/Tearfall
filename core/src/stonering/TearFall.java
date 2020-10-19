package stonering;

import java.util.Optional;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;

import stonering.entity.world.World;
import stonering.game.GameMvc;
import stonering.screen.LocalGenerationScreen;
import stonering.screen.PrepareExpeditionMenu;
import stonering.screen.util.SingleStageScreen;
import stonering.stage.menu.MainMenu;
import stonering.stage.menu.LocationSelectMenu;
import stonering.stage.menu.worldgen.WorldGenMenu;
import stonering.stage.menu.WorldSelectMenu;
import stonering.stage.util.SingleActorStage;
import stonering.util.geometry.Position;
import stonering.widget.GameWithCustomCursor;

/**
 * Game object. Switches screens of a game.
 *
 * @author Alexander Kuzyakov on 08.04.2017.
 */
public class TearFall extends GameWithCustomCursor {
    private PrepareExpeditionMenu prepareExpeditionMenuMvc;
    private LocalGenerationScreen localGenScreen;
    private Texture cursor;

    @Override
    public void create() {
        super.create();
        switchMainMenu();
    }

    public void switchMainMenu() {
        Optional.ofNullable(getScreen()).ifPresent(Screen::dispose);
        showMenuScreen(new MainMenu(this));
    }

    public void switchWorldGenMenu() {
        Optional.ofNullable(getScreen()).ifPresent(Screen::dispose);
        showMenuScreen(new WorldGenMenu(this));
    }

    public void switchWorldsSelectMenu() {
        Optional.ofNullable(getScreen()).ifPresent(Screen::dispose);
        showMenuScreen(new WorldSelectMenu(this));
    }

    public void switchLocationSelectMenu(World world) {
        Optional.ofNullable(getScreen()).ifPresent(Screen::dispose);
        showMenuScreen(new LocationSelectMenu(this, world));
    }

    public void switchPrepareExpeditionMenu(World world, Position location) {
        if (prepareExpeditionMenuMvc == null) prepareExpeditionMenuMvc = new PrepareExpeditionMenu(this);
        prepareExpeditionMenuMvc.setWorld(world);
        prepareExpeditionMenuMvc.setLocation(location);
        setScreen(prepareExpeditionMenuMvc);
    }

    public void switchToLocalGen(World world, Position location) {
        if (localGenScreen == null) localGenScreen = new LocalGenerationScreen(this, world, location);
        setScreen(localGenScreen);
    }

    private void showMenuScreen(Actor menu) {
        SingleActorStage<Actor> stage = new SingleActorStage<>(menu, false);
        stage.setDebugAll(true);
        setScreen(new SingleStageScreen(stage));
        Gdx.input.setInputProcessor(stage);
    }

    public void switchToGame() {
        setScreen(GameMvc.view());
    }
}
