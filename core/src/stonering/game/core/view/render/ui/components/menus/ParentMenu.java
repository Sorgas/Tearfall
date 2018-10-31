package stonering.game.core.view.render.ui.components.menus;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Align;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.components.menus.building.GeneralBuildingMenu;
import stonering.game.core.view.render.ui.components.menus.util.SubMenuMenu;

/**
 * Component of toolbar.
 * All menu commands first dispatched here, and then passed active menu.
 *
 * @author Alexander Kuzyakov on 19.12.2017.
 */
public class ParentMenu extends SubMenuMenu {

    public ParentMenu(GameMvc gameMvc) {
        super(gameMvc);
        hideable = false;
        initTable();
        createMenus();
    }

    private void initTable() {
        this.align(Align.bottom);
    }

    private void createMenus() {
        addMenu(new PlantsMenu(gameMvc), Input.Keys.P, "P: plants");
        addMenu(new DiggingMenu(gameMvc), Input.Keys.O, "O: digging");
        addMenu(new GeneralBuildingMenu(gameMvc),  Input.Keys.I, "building");
    }

    @Override
    public void reset() {

    }
}
