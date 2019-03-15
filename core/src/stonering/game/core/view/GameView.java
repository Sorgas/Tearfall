package stonering.game.core.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import stonering.game.core.view.render.stages.UIDrawer;
import stonering.game.core.view.render.stages.base.BaseStage;
import stonering.game.core.view.render.stages.base.Resizeable;
import stonering.screen.SimpleScreen;
import stonering.util.global.Initable;
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
    private List<Stage> stageList;      // init called on adding.

    //TODO get rid of inits.
    public void init() {
        stageList = new ArrayList<>();
        baseStage = new BaseStage();
        baseStage.init();
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
        getActiveStage().draw();
    }

    public Stage getActiveStage() {
        return stageList.isEmpty() ? baseStage : stageList.get(stageList.size() - 1);
    }

    /**
     * Adds given stage to top of this screen.
     * Stage is inited and resized if possible.
     * @param stage
     */
    public void addStageToList(Stage stage) {
        TagLoggersEnum.UI.logDebug("showing stage " + stage.toString());
        stageList.add(stage);
        if (stage instanceof Initable) ((Initable) stage).init();
        if (stage instanceof Resizeable) ((Resizeable) stage).resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void removeStage(Stage stage) {
        TagLoggersEnum.UI.logDebug("hiding stage " + stage.toString());
        stageList.remove(stage);
        stage.dispose();
    }

    @Override
    public void resize(int width, int height) {
        baseStage.resize(width, height);
        Stage stage = getActiveStage();
        if (stage instanceof Resizeable) ((Resizeable) stage).resize(width, height);
        updateDrawableArea();
    }

    /**
     * Updates visible area in {@link stonering.game.core.view.render.stages.base.LocalWorldDrawer}.
     * Used on resize, {@link stonering.game.core.model.EntitySelector} move and zoom.
     */
    public void updateDrawableArea() {
        baseStage.getWorldDrawer().updateVisibleArea();
    }

    public UIDrawer getUiDrawer() {
        return baseStage.getUiDrawer();
    }

    public BaseStage getBaseStage() {
        return baseStage;
    }
}
