package stonering.stage.toolbar.menus;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Align;
import stonering.stage.toolbar.Toolbar;
import stonering.widget.ToolbarSubMenuMenu;

/**
 * Component of toolbar.
 *
 * @author Alexander Kuzyakov on 19.12.2017.
 */
public class ParentMenu extends ToolbarSubMenuMenu {

    public ParentMenu(Toolbar toolbar) {
        super(toolbar);
        this.align(Align.bottom);
        addMenu(new ToolbarPlantsMenu(toolbar), Input.Keys.P, "plants", "plants_menu");
        addMenu(new ToolbarDiggingMenu(toolbar), Input.Keys.M, "digging", "digging_menu");
        addMenu(new ToolbarBuildingMenu(toolbar), Input.Keys.I, "building", "building_menu");
        addMenu(new ToolbarZonesMenu(toolbar), Input.Keys.U, "zones", "zones_menu");
    }

    @Override // overridden to prevent closing
    public void hide() {
    }
}
