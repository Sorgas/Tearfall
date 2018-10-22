package stonering.game.core.view.render.ui.components.menus.building;

import com.badlogic.gdx.Input;
import stonering.enums.buildings.BuildingMap;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.components.menus.util.SubMenuMenu;

/**
 * ButtonMenu for selecting building category.
 *
 * @author Alexander Kuzyakov on 25.01.2018.
 */
public class GeneralBuildingMenu extends SubMenuMenu {

    public GeneralBuildingMenu(GameMvc gameMvc) {
        super(gameMvc);
        hideable = true;
    }

    @Override
    public void init() {
        createMenus();
        super.init();
    }

    private void createMenus() {
        createCategoryMenu("workbenches", Input.Keys.I);
        createCategoryMenu("constructions", Input.Keys.O);
        createCategoryMenu("furniture", Input.Keys.P);
    }

    private void createCategoryMenu(String category, int hotkey) {
        addMenu(new BuildingCategoryMenu(gameMvc, category), hotkey, category);
    }

    @Override
    public void reset() {

    }
}
