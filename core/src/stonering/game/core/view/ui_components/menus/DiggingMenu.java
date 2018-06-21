package stonering.game.core.view.ui_components.menus;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.designations.DesignationTypes;
import stonering.game.core.GameMvc;
import stonering.game.core.controller.controllers.DesignationsController;

/**
 * ButtonMenu for selecting designation type.
 */
public class DiggingMenu extends ButtonMenu {
    private DesignationsController controller;

    public DiggingMenu(GameMvc gameMvc) {
        super(gameMvc, 1);
        initMenu();
        createTable();
    }

    public void init() {
        super.init();
        controller = gameMvc.getController().getDesignationsController();
    }

    private void initMenu() {
        addButton("D: dig", DesignationTypes.DIG, 'd');
        addButton("R: ramp", DesignationTypes.RAMP, 'r');
        addButton("C: channel", DesignationTypes.CHANNEL, 'c');
        addButton("S: stairs", DesignationTypes.STAIRS, 's');
        addButton("Z: clear", DesignationTypes.NONE, 'z');
    }

    private void createTable() {
        this.pad(10);
        this.right().bottom();
    }

    private void addButton(String text, DesignationTypes type, char hotKey) {
        super.createButton(text, hotKey, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.setActiveDesignation(type);
            }
        });
    }
}
