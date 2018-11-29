package stonering.game.core.view.render.stages;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.utils.Align;
import stonering.entity.local.building.Building;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.menus.workbench.WorkbenchMenu;

/**
 * Stage with menu for building like workbenches and furniture.
 *
 * @author Alexander on 09.11.2018.
 */
public class BuildingStage extends Stage {
    private GameMvc gameMvc;
    private Building building;
    private WorkbenchMenu menu;

    public BuildingStage(GameMvc gameMvc, Building building) {
        this.gameMvc = gameMvc;
        this.building = building;
    }

    public void init() {
        createWorkbenchMenu();
    }

    /**
     * Creates menu for workbench buildings.
     */
    private void createWorkbenchMenu() {
        menu = new WorkbenchMenu(gameMvc, this, building);
        menu.align(Align.center);
        setKeyboardFocus(menu);
        Container container = new Container(menu).bottom().left().pad(10);
        container.setFillParent(true);
        container.setDebug(true, true);
        this.addActor(container);
    }
}
