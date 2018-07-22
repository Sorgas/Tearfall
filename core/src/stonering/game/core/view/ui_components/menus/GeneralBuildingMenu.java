package stonering.game.core.view.ui_components.menus;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.game.core.GameMvc;

/**
 * ButtonMenu for selecting building.
 * <p>
 * @author Alexander Kuzyakov on 25.01.2018.
 */
public class GeneralBuildingMenu extends SubMenuMenu {

    public GeneralBuildingMenu(GameMvc gameMvc) {
        super(gameMvc, true);
        hideable = true;
        initButtons();
    }

    private void initButtons() {
        addButton("P: constructions", Input.Keys.P);
//        addButton("O: workbenches", Input.Keys.O);
//        addButton("I: furniture", Input.Keys.I);
        menus.put(Input.Keys.P, new ConstructionsMenu(gameMvc));
//        menus.put('w', new ConstructionsMenu(gameMvc));
//        menus.put('f', new ConstructionsMenu(gameMvc));
    }

    private void addButton(String text, int hotKey) {
        super.createButton(text, hotKey, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menus.get(hotKey).show();
            }
        });
    }

    @Override
    public void reset() {

    }
}
