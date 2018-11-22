package stonering.game.core.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.scene.LocalWorldDrawer;
import stonering.game.core.view.render.stages.*;
import stonering.game.core.view.render.ui.menus.util.Invokable;
import stonering.game.core.view.render.ui.menus.util.MouseInvocable;
import stonering.utils.global.TagLoggersEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Main game screen. Sprites with general toolbar are rendered on background,
 * additional menus are rendered in separate stages. Only top stage gets input.
 *
 * @author Alexander Kuzyakov on 10.06.2017.
 */
public class GameView implements Screen, Invokable, MouseInvocable {
    private GameMvc gameMvc;
    private BaseStage baseStage; // sprites and toolbar. is always rendered.
    private MainMenu mainMenu;
    private List<InvokableStage> stageList;

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
        stageList = new ArrayList<>();
        baseStage = new BaseStage(gameMvc);
        mainMenu = new MainMenu();
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
        baseStage.draw();
        getActiveStage().draw();
    }

    private void initBatch() {
        baseStage.initBatch();
    }

    private InvokableStage getActiveStage() {
        return stageList.isEmpty() ? baseStage : stageList.get(stageList.size() -1);
    }

    public void addStageToList(InvokableStage stage) {
        TagLoggersEnum.UI.logDebug("showing stage " + stage.toString());
        stageList.add(stage);
        stage.init();
    }

    public void removeStage(InvokableStage stage) {
        TagLoggersEnum.UI.logDebug("hiding stage " + stage.toString());
        stageList.remove(stage);
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

    @Override
    public boolean invoke(int keycode) {
        return getActiveStage().invoke(keycode);
    }

    @Override
    public boolean invoke(int modelX, int modelY, int button, int action) {
        return false;
    }

    public LocalWorldDrawer getWorldDrawer() {
        return baseStage.getWorldDrawer();
    }

    public UIDrawer getUiDrawer() {
        return baseStage.getUiDrawer();
    }
}
