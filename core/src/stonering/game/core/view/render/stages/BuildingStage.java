package stonering.game.core.view.render.stages;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.utils.Align;
import stonering.entity.local.building.Building;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.menus.workbench.WorkbenchMenu;

/**
 * @author Alexander on 09.11.2018.
 */
public class BuildingStage extends InvokableStage {
    private GameMvc gameMvc;
    private Building building;
    private WorkbenchMenu menu;

    public BuildingStage(GameMvc gameMvc, Building building) {
        this.gameMvc = gameMvc;
        this.building = building;
    }

    @Override
    public boolean invoke(int keycode) {
        if(keycode == Input.Keys.Q ) {
            gameMvc.getView().removeStage(this);
            return true;
        }
        return menu.invoke(keycode);
    }

    public void init() {
        createMenu();
    }

    private void createMenu() {
        menu = new WorkbenchMenu(gameMvc, building);
        menu.align(Align.center);
        Container container = new Container(menu).bottom().left().pad(10);
        container.setFillParent(true);
        this.addActor(container);
    }
}
