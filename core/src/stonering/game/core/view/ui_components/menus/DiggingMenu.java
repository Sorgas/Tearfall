package stonering.game.core.view.ui_components.menus;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.designations.DesignationTypes;
import stonering.game.core.controller.controllers.DesignationsController;
import stonering.utils.global.StaticSkin;

/**
 * Menu for selecting designation type.
 */
public class DiggingMenu extends ButtonMenu {
    private DesignationsController controller;

    public DiggingMenu() {
        super();
        initMenu();
        createTable();
    }

    private void initMenu() {
        createButton("D: dig", DesignationTypes.DIG, 'd');
        createButton("R: ramp", DesignationTypes.RAMP, 'r');
        createButton("C: channel", DesignationTypes.CHANNEL, 'c');
        createButton("S: stairs", DesignationTypes.STAIRS, 's');
        createButton("Z: clear", DesignationTypes.NONE, 'z');
        createButton("ESC: cancel", null, (char) 27);
    }

    private void createTable() {
        this.defaults().padBottom(5).fillX();
        this.pad(10);
        this.right().bottom();
    }

    private void createButton(String text, DesignationTypes type, char hotKey) {
        TextButton button = new TextButton(text, StaticSkin.getSkin());
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.setActiveDesignation(type);
            }
        });
        addButton(button, hotKey);
    }

    public void setController(DesignationsController controller) {
        this.controller = controller;
    }
}
