package stonering.game.core.view.render.ui.menus;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Align;
import stonering.game.core.view.render.ui.menus.building.GeneralBuildingMenu;
import stonering.game.core.view.render.ui.menus.util.SubMenuMenu;
import stonering.game.core.view.render.ui.menus.zone.ZonesMenu;

/**
 * Component of toolbar.
 * All screen commands first dispatched here, and then passed active screen.
 *
 * @author Alexander Kuzyakov on 19.12.2017.
 */
public class ParentMenu extends SubMenuMenu {

    public ParentMenu() {
        hideable = false;
        initTable();
        createMenus();
    }

    private void initTable() {
        this.align(Align.bottom);
    }

    private void createMenus() {
        addMenu(new PlantsMenu(), Input.Keys.P, "P: plants");
        addMenu(new DiggingMenu(), Input.Keys.O, "O: digging");
        addMenu(new GeneralBuildingMenu(),  Input.Keys.I, "I: building");
        addMenu(new ZonesMenu(), Input.Keys.U, "U: zones");
    }

    @Override
    public void reset() {

    }
}
