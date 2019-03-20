package stonering.game.view.render.ui.menus.zone;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import stonering.util.global.StaticSkin;

/**
 * Menu for managing farms. Plants for growing are configured from here.
 *
 * @author Alexander on 20.03.2019.
 */
public class FarmZoneMenu extends Window {

    public FarmZoneMenu() {
        super("qwer", StaticSkin.getSkin());

    }

    private void createTable() {
        setDebug(true, true);

        add(createOrderList().fill()).prefWidth(600).prefHeight(200).expandX();
        add(createCloseButton()).prefWidth(20).prefHeight(20).right().top().row();

        HorizontalGroup horizontalGroup = new HorizontalGroup();
        horizontalGroup.addActor(createAddButton());
        horizontalGroup.addActor(hintLabel = new Label(MENU_HINT, StaticSkin.getSkin()));
        add(horizontalGroup).prefHeight(20).left().top();

        setWidth(800);
        setHeight(600);
    }
}
