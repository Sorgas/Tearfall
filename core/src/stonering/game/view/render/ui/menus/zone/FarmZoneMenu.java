package stonering.game.view.render.ui.menus.zone;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import stonering.entity.local.plants.Plant;
import stonering.util.global.StaticSkin;

/**
 * Menu for managing farms. Plants for growing are configured from here.
 *
 * @author Alexander on 20.03.2019.
 */
public class FarmZoneMenu extends Window {
    private List<Plant> enabledPlants;
    private List<Plant> disabledPlants;
    private HorizontalGroup bottomButtons;

    public FarmZoneMenu() {
        super("qwer", StaticSkin.getSkin());
        createTable();
    }

    private void createTable() {
        setDebug(true, true);
        enabledPlants = new List<>(StaticSkin.getSkin());

        add(enabledPlants).prefWidth(Value.percentWidth(0.5f, this))
                .prefHeight(Value.percentHeight(0.5f, this));
        add(disabledPlants).prefWidth(Value.percentWidth(0.5f, this))
                .prefHeight(Value.percentHeight(0.5f, this));

        bottomButtons = new HorizontalGroup();
        horizontalGroup.addActor(createAddButton());
        horizontalGroup.addActor(hintLabel = new Label(MENU_HINT, StaticSkin.getSkin()));
        add(horizontalGroup).prefHeight(20).left().top();

        setWidth(800);
        setHeight(600);
    }
}
