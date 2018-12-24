package stonering.game.core.view.render.stages;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.utils.Align;
import stonering.entity.local.building.Building;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.menus.workbench.WorkbenchMenu;

/**
 * Stage with menu for building like workbenches and furniture.
 * Its keyboard focus is updated by ui elements.
 * Game is paused while menu is shown.
 *
 * @author Alexander on 09.11.2018.
 */
public class BuildingStage extends InitableStage {
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
        menu.updateStageFocus(menu);
        gameMvc.getController().setCameraEnabled(false);
//        wasPaused = gameMvc.getModel().isPaused();
//        gameMvc.getModel().setPaused(true);
//        gameMvc.getController().getPauseInputAdapter().setEnabled(false);
    }

    /**
     * Creates menu for workbench buildings and adds it to stage.
     */
    private void createWorkbenchMenu() {
        menu = new WorkbenchMenu(gameMvc, building);
        menu.align(Align.center);
        Container container = new Container(menu).center();
        container.setFillParent(true);
        container.setDebug(true, true);
        this.addActor(container);
    }

    @Override
    public void dispose() {
        gameMvc.getView().removeStage(this);
        gameMvc.getController().setCameraEnabled(true);
//        gameMvc.getController().getPauseInputAdapter().setEnabled(true);
//        gameMvc.getModel().setPaused(wasPaused);
        super.dispose();
    }
}
