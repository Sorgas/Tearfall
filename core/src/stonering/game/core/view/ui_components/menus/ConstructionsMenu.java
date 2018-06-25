package stonering.game.core.view.ui_components.menus;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.ConstructionsEnum;
import stonering.game.core.GameMvc;

/**
 * ButtonMenu with buttons
 *
 * @author Alexander Kuzyakov
 *         created on 15.06.2018
 */
public class ConstructionsMenu extends ButtonMenu {
    public ConstructionsMenu(GameMvc gameMvc) {
        super(gameMvc, 1);
        hideable = true;
        initMenu();
    }

    @Override
    public void init() {
        super.init();

    }

    private void initMenu() {
        addButton("W: wall", ConstructionsEnum.WALL, 'w');
        addButton("F: floor", ConstructionsEnum.FLOOR, 'f');
        addButton("R: ramp", ConstructionsEnum.RAMP, 'r');
        addButton("S: stairs", ConstructionsEnum.STAIRS, 's');
    }

    private void addButton(String text, ConstructionsEnum type, char hotKey) {
        super.createButton(text, hotKey, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
    }

    @Override
    public void reset() {

    }
}
