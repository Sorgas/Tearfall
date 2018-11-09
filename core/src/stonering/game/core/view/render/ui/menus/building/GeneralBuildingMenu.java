package stonering.game.core.view.render.ui.menus.building;

import com.badlogic.gdx.Input;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.menus.util.SubMenuMenu;

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
        addMenu(new BuildingCategoryMenu(gameMvc, "workbenches"), Input.Keys.I, "workbenches");
        addMenu(new BuildingCategoryMenu(gameMvc, "constructions"), Input.Keys.O, "constructions");
        addMenu(new BuildingCategoryMenu(gameMvc, "furniture"), Input.Keys.P, "furniture");
    }

    @Override
    public void reset() {

    }
}
