package stonering.game.view.render.ui.menus.zone;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import stonering.entity.local.plants.Plant;
import stonering.game.view.render.ui.lists.NavigableList;
import stonering.util.global.StaticSkin;

/**
 * Menu for managing farms. Plants for growing are configured from here.
 *
 * @author Alexander on 20.03.2019.
 */
public class FarmZoneMenu extends Window {
    private NavigableList<Plant> enabledPlants;
    private NavigableList<Plant> disabledPlants;
    private HorizontalGroup bottomButtons;

    public FarmZoneMenu() {
        super("qwer", StaticSkin.getSkin());
        createTable();
    }

    private void createTable() {
        setDebug(true, true);
        disabledPlants = new NavigableList<>();
        add(new Label("All plants:", StaticSkin.getSkin()));
        add(new Label("Selected plants:", StaticSkin.getSkin()));
        add(enabledPlants).prefWidth(Value.percentWidth(0.5f, this)).prefHeight(Value.percentHeight(0.5f, this));
        add(disabledPlants).prefWidth(Value.percentWidth(0.5f, this)).prefHeight(Value.percentHeight(0.5f, this));
        setWidth(800);
        setHeight(600);
    }

    private void createList() {
        enabledPlants = new NavigableList<>();
        enabledPlants.setHighlightHandler(focused -> {});
        enabledPlants.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch(keycode) {
                    case Input.Keys.A: {

                    }
                }
                return super.keyDown(event, keycode);
            }
        });
    }
}
