package stonering.game.view.render.stages;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.utils.Align;
import stonering.entity.building.Building;
import stonering.game.GameMvc;
import stonering.game.view.render.ui.menus.workbench.WorkbenchMenu;
import stonering.util.global.Initable;
import stonering.util.global.Logger;

/**
 * Stage with screen for building like workbenches and furniture.
 * Its keyboard focus is updated by ui elements.
 * Game is paused while screen is shown.
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
        createWorkbenchMenu();
        setKeyboardFocus(menu);
        gameMvc.getController().setCameraEnabled(false);
        wasPaused = gameMvc.getModel().isPaused();
        gameMvc.getModel().setPaused(true);
        gameMvc.getController().getPauseInputAdapter().setEnabled(false);
    }

    /**
     * Creates screen for workbench buildings and adds it to stage.
     */
    private void createWorkbenchMenu() {
        menu = new WorkbenchMenu(building);
        menu.align(Align.center);
        Container container = new Container(menu).center();
        container.setFillParent(true);
        container.setDebug(true, true);
        this.addActor(container);
    }

    @Override
    public void dispose() {
        gameMvc.getController().setCameraEnabled(true);
        wasPaused = gameMvc.getModel().isPaused();
        gameMvc.getModel().setPaused(wasPaused);
        gameMvc.getController().getPauseInputAdapter().setEnabled(true);
        super.dispose();
    }

    @Override
    public void resize(int width, int height) {
        Logger.UI.logDebug("resizing Building stage to " + width + " " + height);
        super.resize(width, height);
        menu.setWidth(width / 2);
        menu.setHeight(height / 2);
    }
}
