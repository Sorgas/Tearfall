package stonering.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import stonering.game.GameMvc;
import stonering.game.controller.controllers.StageInputAdapter;
import stonering.game.model.EntitySelector;
import stonering.stage.toolbar.MainUiStage;
import stonering.stage.MapEntitySelectStage;
import stonering.stage.pause.PauseMenuStage;
import stonering.stage.localworld.LocalWorldStage;
import stonering.widget.util.Resizeable;
import stonering.screen.SimpleScreen;
import stonering.util.geometry.Position;
import stonering.util.global.Initable;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Main game screen.
 * Contains current stages sequence for rendering (localWorldStage and mainUiStage are always rendered on images).
 * Additional menus are rendered in separate stages.
 *
 * @author Alexander Kuzyakov on 10.06.2017.
 */
public class GameView extends SimpleScreen implements Initable {
    private LocalWorldStage localWorldStage;
    private MainUiStage mainUiStage;
    private List<Stage> stageList;      // init called on adding.

    //TODO get rid of inits.
    public void init() {
        stageList = new ArrayList<>();
        localWorldStage = new LocalWorldStage();
        mainUiStage = new MainUiStage();
        mainUiStage.init();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
        localWorldStage.act(delta);
        localWorldStage.draw();
        mainUiStage.act(delta);
        mainUiStage.draw();
        Stage activeStage = getActiveStage();
        if(activeStage != null) activeStage.draw();
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
        Logger.UI.logDebug("showing stage " + stage.toString());
        stageList.add(stage);
        if (stage instanceof Initable) ((Initable) stage).init();
        if (stage instanceof Resizeable) ((Resizeable) stage).resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void removeStage(Stage stage) {
        Logger.UI.logDebug("hiding stage " + stage.toString());
        stageList.remove(stage);
        stage.dispose();
    }

    @Override
    public void resize(int width, int height) {
        localWorldStage.resize(width, height);
        mainUiStage.resize(width, height);
        Stage stage = getActiveStage();
        if (stage instanceof Resizeable) ((Resizeable) stage).resize(width, height);
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
     * Called, if toolbar didn't handle event, shows selection list for map tile.
     */
    //TODO add filters like Shift+E Ctrl+E etc, move to mainUiStage
    private boolean handleKeyDown(int keyCode) {
        switch (keyCode) {
            case Input.Keys.ESCAPE:
                GameMvc.instance().getView().addStageToList(new PauseMenuStage());
                return true;
            case Input.Keys.E:
                showMapEntityListStage(GameMvc.instance().getModel().get(EntitySelector.class).getPosition());
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
        addStageToList(new MapEntitySelectStage(position, MapEntitySelectStage.NONE));
    }

    public MainUiStage getUiDrawer() {
        return mainUiStage;
    }

    public LocalWorldStage getBaseStage() {
        return localWorldStage;
    }
}
