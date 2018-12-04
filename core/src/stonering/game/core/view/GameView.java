package stonering.game.core.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import stonering.game.core.GameMvc;
import stonering.game.core.controller.controllers.GameInputHandler;
import stonering.game.core.view.render.scene.LocalWorldDrawer;
import stonering.game.core.view.render.stages.*;
import stonering.utils.global.TagLoggersEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Main game screen. Sprites with general toolbar are rendered on background,
 * additional menus are rendered in separate stages.
 * Has InputAdapter for traversing input. Only top stage gets input.
 *
 * @author Alexander Kuzyakov on 10.06.2017.
 */
public class GameView extends InputAdapter implements Screen {
    private GameMvc gameMvc;
    private GameInputHandler inputHandler;      // handles case for skipping keyTyped after keyDown
    private BaseStage baseStage;                // sprites and toolbar. is always rendered.
    private MainMenu mainMenu;
    private List<InitableStage> stageList;      // init called on adding.

    /**
     * Also creates all sub-components.
     *
     * @param gameMvc
     */
    public GameView(GameMvc gameMvc) {
        this.gameMvc = gameMvc;
        inputHandler = new GameInputHandler(gameMvc);
        createStages();
    }

    private void createStages() {
        stageList = new ArrayList<>();
        baseStage = new BaseStage(gameMvc);
        mainMenu = new MainMenu();
        inputHandler.setStage(getActiveStage());    // update stage to receive input
    }

    /**
     * Do bindings of components to their controllers/models.
     */
    public void init() {
        baseStage.init();
        mainMenu.init();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
        if(getActiveStage() != baseStage) {
            baseStage.getViewport().apply();
            baseStage.act();
            baseStage.draw();
        }
        getActiveStage().getViewport().apply();
        getActiveStage().act();
        getActiveStage().draw();
    }

    private void initBatch() {
        baseStage.initBatch();
    }

    private InitableStage getActiveStage() {
        return stageList.isEmpty() ? baseStage : stageList.get(stageList.size() -1);
    }

    /**
     * Adds given stage to top of this screen. Updates inputHandler.
     * @param stage
     */
    public void addStageToList(InitableStage stage) {
        TagLoggersEnum.UI.logDebug("showing stage " + stage.toString());
        stageList.add(stage);

        stage.init();
        inputHandler.setStage(getActiveStage());  // update stage to receive input
    }

    public void removeStage(Stage stage) {
        TagLoggersEnum.UI.logDebug("hiding stage " + stage.toString());
        stageList.remove(stage);
        inputHandler.setStage(getActiveStage());  // update stage to receive input
    }

    @Override
    public void resize(int width, int height) {
        baseStage.resize(width, height);
        initBatch();
    }

    @Override
    public void show() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        baseStage.disposeBatch();
    }

    public LocalWorldDrawer getWorldDrawer() {
        return baseStage.getWorldDrawer();
    }

    public UIDrawer getUiDrawer() {
        return baseStage.getUiDrawer();
    }

    @Override
    public boolean keyDown(int keycode) {
        return inputHandler.keyDown(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        return inputHandler.keyTyped(character);
    }
}
