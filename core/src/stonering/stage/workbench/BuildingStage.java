package stonering.stage.workbench;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.utils.Align;
import stonering.entity.building.Building;
import stonering.game.GameMvc;
import stonering.stage.UiStage;
import stonering.util.global.Initable;
import stonering.util.global.Logger;

/**
 * Stage with menu for building like workbenches and furniture.
 * Its keyboard focus is updated by ui elements.
 * Game is paused while menu is shown.
 *
 * @author Alexander on 09.11.2018.
 */
public class BuildingStage extends UiStage implements Initable {
    private GameMvc gameMvc;
    private Building building;
    private WorkbenchMenu menu;
    private boolean wasPaused;

    public BuildingStage(GameMvc gameMvc, Building building) {
        this.gameMvc = gameMvc;
        this.building = building;
    }

    @Override
    public void init() {
        gameMvc.getController().setCameraEnabled(false);
        wasPaused = gameMvc.getModel().isPaused(); // used for unpausing when menu is closed
        gameMvc.getModel().setPaused(true);
        gameMvc.getController().pauseInputAdapter.setEnabled(false);
        createWorkbenchMenu();
    }

    /**
     * Creates menu for workbench buildings and adds it to stage.
     */
    private void createWorkbenchMenu() {
        menu = new WorkbenchMenu(building);
        menu.align(Align.center);
        Container container = new Container<>(menu).center();
        container.setFillParent(true);
        container.setDebug(true, true);
        this.addActor(container);
        menu.initFocus();
    }

    @Override
    public void dispose() {
        gameMvc.getController().setCameraEnabled(true);
        wasPaused = gameMvc.getModel().isPaused();
        gameMvc.getModel().setPaused(wasPaused);
        gameMvc.getController().pauseInputAdapter.setEnabled(true);
        super.dispose();
    }

    @Override
    public void resize(int width, int height) {
        Logger.UI.logDebug("resizing Building stage to " + width + " " + height);
        super.resize(width, height);
    }
}
