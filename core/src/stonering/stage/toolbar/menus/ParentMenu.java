package stonering.stage.toolbar.menus;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Align;
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
        createMenus();
    }

    private void createMenus() {
        addMenu(new PlantsMenu(toolbar), Input.Keys.P, "P: plants");
        addMenu(new DiggingMenu(toolbar), Input.Keys.O, "O: digging");
        addMenu(new ToolbarBuildingMenu(toolbar), Input.Keys.I, "I: building");
        addMenu(new ZonesMenu(toolbar), Input.Keys.U, "U: zones");
    }

    /**
     * Overrode to prevent closing.
     */
    @Override
    public void hide() {}
}
