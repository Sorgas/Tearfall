package stonering.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import stonering.game.controller.controllers.StageInputAdapter;
import stonering.stage.toolbar.MainUiStage;
import stonering.stage.MapEntitySelectStage;
import stonering.stage.pause.PauseMenuStage;
import stonering.stage.localworld.LocalWorldStage;
import stonering.widget.util.Resizeable;
import stonering.util.geometry.Position;
import stonering.util.global.Initable;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Main game screen.
 * Contains current stages sequence for rendering (localWorldStage and mainUiStage are always rendered).
 * Additional menus are displayed in separate stages on top of default ones.
 *
 * @author Alexander Kuzyakov on 10.06.2017.
 */
public class GameView extends SimpleScreen {
    public final LocalWorldStage localWorldStage;
    public final MainUiStage mainUiStage;

    public final List<Stage> stageList;      // init called on adding.
    public final StageInputAdapter stageInputAdapter;

    public GameView() {
        stageList = new ArrayList<>();
        stageList.add(localWorldStage = new LocalWorldStage());
        stageList.add(mainUiStage = new MainUiStage());
        stageInputAdapter = new StageInputAdapter(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
        for (Stage stage : stageList) {
            stage.act(delta);
            stage.draw();
        }
    }

    public Stage getActiveStage() {
        return stageList.get(stageList.size() - 1);
    }

    /**
     * Adds given stage to top of this screen.
     * Stage is inited and resized if possible.
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

    public void showPauseMenu() {
        addStageToList(new PauseMenuStage());
    }

    public void showEntityStage(Position position) {
        MapEntitySelectStage stage = new MapEntitySelectStage();
        addStageToList(stage);
        stage.showEntitySelectList(position);
    }
}
