package stonering.game.core.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import stonering.game.core.view.render.stages.InitableStage;
import stonering.game.core.view.render.stages.MainMenu;
import stonering.game.core.view.render.stages.UIDrawer;
import stonering.game.core.view.render.stages.base.BaseStage;
import stonering.game.core.view.render.stages.base.LocalWorldDrawer;
import stonering.screen.SimpleScreen;
import stonering.util.global.TagLoggersEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Main game screen. Sprites with general toolbar are rendered on background,
 * additional menus are rendered in separate stages.
 *
 * @author Alexander Kuzyakov on 10.06.2017.
 */
public class GameView extends SimpleScreen {
    private BaseStage baseStage;                // sprites and toolbar. is always rendered.
    private MainMenu mainMenu;
    private List<InitableStage> stageList;      // init called on adding.

    /**
     * TODO get rid of inits.
     */
    public void init() {
        stageList = new ArrayList<>();
        baseStage = new BaseStage();
        baseStage.init();
        mainMenu = new MainMenu();
        mainMenu.init();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
        if (getActiveStage() != baseStage) {   // render base stage under other
            baseStage.getViewport().apply();
            baseStage.act(delta);
            baseStage.draw();
        }
        getActiveStage().getViewport().apply();
        getActiveStage().act(delta);
        getActiveStage().draw();
    }

    public Stage getActiveStage() {
        return stageList.isEmpty() ? baseStage : stageList.get(stageList.size() - 1);
    }

    /**
     * Adds given stage to top of this screen.
     *
     * @param stage
     */
    public void addStageToList(InitableStage stage) {
        TagLoggersEnum.UI.logDebug("showing stage " + stage.toString());
        stageList.add(stage);
        stage.init();
    }

    public void removeStage(Stage stage) {
        TagLoggersEnum.UI.logDebug("hiding stage " + stage.toString());
        stageList.remove(stage);
        stage.dispose();
    }

    //TODO fix resize
    @Override
    public void resize(int width, int height) {
        baseStage.resize(width, height);
        baseStage.initBatch();
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
}
