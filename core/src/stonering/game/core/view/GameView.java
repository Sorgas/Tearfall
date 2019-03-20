package stonering.game.core.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import stonering.game.core.GameMvc;
import stonering.game.core.controller.controllers.StageInputAdapter;
import stonering.game.core.model.EntitySelector;
import stonering.game.core.view.render.stages.MainUiStage;
import stonering.game.core.view.render.stages.MapEntitySelectStage;
import stonering.game.core.view.render.stages.PauseMenuStage;
import stonering.game.core.view.render.stages.base.LocalWorldStage;
import stonering.game.core.view.render.stages.base.Resizeable;
import stonering.screen.SimpleScreen;
import stonering.util.geometry.Position;
import stonering.util.global.Initable;
import stonering.util.global.TagLoggersEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Main game screen.
 * Contains current stages sequence for rendering (localWorldStage and mainUiStage are always rendered on background).
 * Additional menus are rendered in separate stages.
 *
 * @author Alexander Kuzyakov on 10.06.2017.
 */
public class GameView extends SimpleScreen {
    private LocalWorldStage localWorldStage;
    private MainUiStage mainUiStage;
    private List<Stage> stageList;      // init called on adding.

    //TODO get rid of inits.
    public void init() {
        stageList = new ArrayList<>();
        localWorldStage = new LocalWorldStage();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
        localWorldStage.act(delta);
        localWorldStage.draw();
        mainUiStage.act(delta);
        mainUiStage.draw();
        getActiveStage().draw();
    }

    public Stage getActiveStage() {
        return stageList.isEmpty() ? null : stageList.get(stageList.size() - 1);
    }

    /**
     * Adds given stage to top of this screen.
     * Stage is inited and resized if possible.
     *
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
        localWorldStage.resize(width, height);
        mainUiStage.resize(width, height);
        Stage stage = getActiveStage();
        if (stage instanceof Resizeable) ((Resizeable) stage).resize(width, height);
        updateDrawableArea();
    }

    /**
     * Handles input from {@link StageInputAdapter}.
     */
    public boolean keyDown(int keyCode) {
        return (getActiveStage() != null && getActiveStage().keyDown(keyCode)) || //try handle in active stage
                mainUiStage.keyDown(keyCode) || // try handle in ui stage
                handleKeyDown(keyCode);
    }

    /**
     * Called, if toolbar didn't handle event, shows selection list for map tile or pause menu.
     */
    //TODO add filters like Shift+E Ctrl+E etc
    private boolean handleKeyDown(int keyCode) {
        switch (keyCode) {
            case Input.Keys.Q:
                GameMvc.getInstance().getView().addStageToList(new PauseMenuStage());
                return true;
            case Input.Keys.E:
                showMapEntityListStage(GameMvc.getInstance().getModel().get(EntitySelector.class).getPosition());
                return true;
        }
        return false;
    }

    /**
     * Shows stage with list of entities in given position.
     * If there is only one, proceeds to entity stage immediately.
     */
    //TODO add filters
    private void showMapEntityListStage(Position position) {
        addStageToList(new MapEntitySelectStage(position, -1));
    }

    /**
     * Updates visible area in {@link LocalWorldStage}.
     * Used on resize, {@link stonering.game.core.model.EntitySelector} move and zoom.
     */
    public void updateDrawableArea() {
        localWorldStage.updateVisibleArea();
    }

    public MainUiStage getUiDrawer() {
        return mainUiStage;
    }

    public LocalWorldStage getBaseStage() {
        return localWorldStage;
    }
}
