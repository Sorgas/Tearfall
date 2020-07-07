package stonering.screen;

import com.badlogic.gdx.scenes.scene2d.Stage;
import stonering.game.controller.inputProcessors.StageInputAdapter;
import stonering.stage.OverlayStage;
import stonering.stage.util.UiStage;
import stonering.stage.toolbar.ToolbarStage;
import stonering.stage.MapEntitySelectStage;
import stonering.stage.pause.PauseMenuStage;
import stonering.stage.localworld.LocalWorldStage;
import stonering.util.geometry.Int3dBounds;

/**
 * Main game screen.
 * Contains current stages sequence for rendering (localWorldStage and mainUiStage are always rendered).
 * Additional menus are displayed in separate stages on top of default ones.
 *
 * @author Alexander Kuzyakov on 10.06.2017.
 */
public class GameView extends MultiStageScreen {
    public final OverlayStage overlayStage;
    public final ToolbarStage toolbarStage;
    public final LocalWorldStage localWorldStage;
    public final StageInputAdapter stageInputAdapter;
    public boolean showOverlay = true;
    
    public GameView() {
        stageList.add(localWorldStage = new LocalWorldStage());
        stageList.add(toolbarStage = new ToolbarStage());
        stageInputAdapter = new StageInputAdapter(this);
        overlayStage = new OverlayStage();
    }
    
    public Stage getActiveStage() {
        return stageList.get(stageList.size() - 1);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if(showOverlay) {
            overlayStage.act(delta);
            overlayStage.draw();
        }
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
            if (stage == exceptStage) continue;
            if (stage instanceof UiStage) ((UiStage) stage).setUiScale(value);
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        overlayStage.resize(width, height);
    }
}
