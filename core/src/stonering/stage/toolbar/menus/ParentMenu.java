package stonering.stage.toolbar.menus;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Align;
import stonering.stage.toolbar.Toolbar;
import stonering.widget.ToolbarSubmenuMenu;

/**
 * Component of toolbar.
 *
 * @author Alexander Kuzyakov on 19.12.2017.
 */
public class ParentMenu extends ToolbarSubmenuMenu {

    public ParentMenu(Toolbar toolbar) {
        super(toolbar);
        addSubmenu(new ToolbarPlantsMenu(toolbar), Input.Keys.P, "P: plants", "plants_menu");
        addSubmenu(new ToolbarDiggingMenu(toolbar), Input.Keys.M, "M: digging", "digging_menu");
        addSubmenu(new ToolbarBuildingMenu(toolbar), Input.Keys.B, "B: building", "building_menu");
        addSubmenu(new ToolbarZonesMenu(toolbar), Input.Keys.U, "U: zones", "zones_menu");
    }

    @Override // overridden to prevent closing
    public void hide() {
    }
}
