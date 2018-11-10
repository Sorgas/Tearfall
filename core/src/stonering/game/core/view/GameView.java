package stonering.game.core.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.scene.LocalWorldDrawer;
import stonering.game.core.view.render.stages.*;
import stonering.game.core.view.render.ui.menus.util.Invokable;
import stonering.game.core.view.render.ui.menus.util.MouseInvocable;

/**
 * Main game screen. Sprites with general toolbar are rendered on background,
 * additional menus are rendered in separate stages. Only top stage gets input.
 *
 * @author Alexander Kuzyakov on 10.06.2017.
 */
public class GameView implements Screen, Invokable, MouseInvocable {
    private GameMvc gameMvc;
    private BaseStage baseStage; // sprites and toolbar.
    private MainMenu mainMenu;
    private WorkbenchStage workbenchStage;

    /**
     * Also creates all sub-components.
     *
     * @param gameMvc
     */
    public GameView(GameMvc gameMvc) {
        this.gameMvc = gameMvc;
        createStages();
    }

    private void createStages() {
        baseStage = new BaseStage(gameMvc);
        mainMenu = new MainMenu();
        workbenchStage= new WorkbenchStage();
    }

    /**
     * Do bindings of components to their controllers/models.
     */
    public void init() {
        baseStage.init();
        mainMenu.init();
        workbenchStage.init();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
        baseStage.draw();
        getActiveStage().draw();
    }

    @Override
    public void resize(int width, int height) {
//        uiDrawer.resize(width, height);
        initBatch();
    }

    private void initBatch() {
        baseStage.initBatch();
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

    public UIDrawer getUiDrawer() {
        return baseStage.getUiDrawer();
    }

    public LocalWorldDrawer getWorldDrawer() {
        return baseStage.getWorldDrawer();
    }

    @Override
    public boolean invoke(int keycode) {
        if (!getActiveStage().invoke(keycode)) { // first priority, returns false if no menus open

        }
        return false;
    }

    @Override
    public boolean invoke(int modelX, int modelY, int button, int action) {
//        if (!getActiveStage().invoke(modelX, modelY, button, action)) {
//        }
        return false;
    }

    private InvokableStage getActiveStage() {
        return baseStage;
    }
}
