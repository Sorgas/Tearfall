package stonering.game.core.view.ui_components.menus;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.designations.DesignationTypes;
import stonering.game.core.controller.controllers.DesignationsController;
import stonering.utils.global.StaticSkin;

public class DiggingMenu extends Menu {
    private DesignationsController controller;

    public DiggingMenu() {
        super();
        createTable();
    }

    private void createTable() {
        this.defaults().padBottom(5).fillX();
        this.pad(10);
        this.right().bottom();

        addButtonToTable("D: dig", DesignationTypes.DIG, 'd');
        addButtonToTable("R: ramp", DesignationTypes.RAMP, 'r');
        addButtonToTable("C: channel", DesignationTypes.CHANNEL, 'c');
        addButtonToTable("S: stairs", DesignationTypes.STAIRS, 's');
        addButtonToTable("Z: clear", DesignationTypes.NONE, 'z');
        addButtonToTable("ESC: cancel", null, (char) 27);
    }

    private void addButtonToTable(String text, DesignationTypes type, char hotKey) {
        TextButton button = new TextButton(text, StaticSkin.getSkin());
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.setActiveDesignation(type);
                toolbar.closeMenus();
            }
        });
        this.add(button).row();
        hotkeyMap.put(hotKey, button);
    }

    public void setController(DesignationsController controller) {
        this.controller = controller;
    }
}
