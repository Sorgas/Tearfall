package stonering.game.core.view.ui_components.menus;

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
        super(gameMvc, 1);
        hideable = true;
        initButtons();
    }

    private void initButtons() {
        addButton("C: constructions", 'c');
        addButton("W: workbenches", 'w');
        addButton("F: furniture", 'f');
        menus.put('c', new ConstructionsMenu(gameMvc));
//        menus.put('w', new ConstructionsMenu(gameMvc));
//        menus.put('f', new ConstructionsMenu(gameMvc));
    }

    private void addButton(String text, char hotKey) {
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
