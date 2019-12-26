package stonering.screen;

import com.badlogic.gdx.scenes.scene2d.Stage;
import stonering.game.controller.controllers.StageInputAdapter;
import stonering.stage.toolbar.MainUiStage;
import stonering.stage.MapEntitySelectStage;
import stonering.stage.pause.PauseMenuStage;
import stonering.stage.localworld.LocalWorldStage;
import stonering.util.ui.MultiStageScreen;
import stonering.util.geometry.Position;
import stonering.util.global.Initable;
import stonering.util.global.Logger;

/**
 * Main game screen.
 * Contains current stages sequence for rendering (localWorldStage and mainUiStage are always rendered).
 * Additional menus are displayed in separate stages on top of default ones.
 *
 * @author Alexander Kuzyakov on 10.06.2017.
 */
public class GameView extends MultiStageScreen {
    public final LocalWorldStage localWorldStage;
    public final MainUiStage mainUiStage;
    public final StageInputAdapter stageInputAdapter;

    public GameView() {
        stageList.add(localWorldStage = new LocalWorldStage());
        stageList.add(mainUiStage = new MainUiStage());
        stageInputAdapter = new StageInputAdapter(this);
    }

    public Stage getActiveStage() {
        return stageList.get(stageList.size() - 1);
    }

    /**
     * Inits added stage
     */
    @Override
    public void addStage(Stage stage) {
        Logger.UI.logDebug("adding stage " + stage.toString() + " to view.");
        super.addStage(stage);
        if (stage instanceof Initable) ((Initable) stage).init();
    }

    public void showPauseMenu() {
        addStage(new PauseMenuStage());
    }

    public void showEntityStage(Position position) {
        MapEntitySelectStage stage = new MapEntitySelectStage();
        addStage(stage);
        stage.showEntitySelectList(position);
    }
}
