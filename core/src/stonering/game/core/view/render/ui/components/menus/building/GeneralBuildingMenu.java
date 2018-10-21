package stonering.game.core.view.render.ui.components.menus.building;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.components.menus.util.ItemMenu;
import stonering.game.core.view.render.ui.components.menus.util.MenuItem;

/**
 * ButtonMenu for selecting building category.
 * <p>
 * @author Alexander Kuzyakov on 25.01.2018.
 */
public class GeneralBuildingMenu extends ItemMenu {

    public GeneralBuildingMenu(GameMvc gameMvc) {
        super(gameMvc, true);
        hideable = true;
        initButtons();
    }

    private void initButtons() {
        addButton("P: constructions", Input.Keys.P);
        addButton("O: workbenches", Input.Keys.O);
//        addButton("I: furniture", Input.Keys.I);
        items.put(Input.Keys.P, new MenuItem(new BuildingCategoryMenu(gameMvc)));
//        menus.put('w', new ConstructionsMenu(gameMvc));
//        menus.put('f', new ConstructionsMenu(gameMvc));
    }

    private void addButton(String text, int hotKey) {
        super.createButton(text, hotKey, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                items.get(hotKey).call();
            }
        });
    }

    @Override
    public void reset() {

    }
}
