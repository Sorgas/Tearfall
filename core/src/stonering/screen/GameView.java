package stonering.screen;

import com.badlogic.gdx.scenes.scene2d.Stage;
import stonering.game.controller.inputProcessors.StageInputAdapter;
import stonering.stage.UiStage;
import stonering.stage.toolbar.ToolbarStage;
import stonering.stage.MapEntitySelectStage;
import stonering.stage.pause.PauseMenuStage;
import stonering.stage.localworld.LocalWorldStage;
import stonering.util.geometry.Int3dBounds;
import stonering.util.ui.MultiStageScreen;
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
    public final ToolbarStage toolbarStage;
    public final StageInputAdapter stageInputAdapter;

    public GameView() {
        stageList.add(localWorldStage = new LocalWorldStage());
        stageList.add(toolbarStage = new ToolbarStage());
        stageInputAdapter = new StageInputAdapter(this);
    }

    public Stage getActiveStage() {
        return stageList.get(stageList.size() - 1);
    }

    /**
     * Inits added stage.
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

    public void showEntityStage(Int3dBounds box) {
        MapEntitySelectStage stage = new MapEntitySelectStage();
        addStage(stage);
        stage.showEntitySelectList(box);
    }

    public void updateStagesScale(float value, Stage exceptStage) {
        for (Stage stage : stageList) {
            if(stage == exceptStage) continue;
            if(stage instanceof UiStage) ((UiStage) stage).setUiScale(value);
        }
    }
}
