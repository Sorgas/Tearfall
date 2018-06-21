package stonering.game.core.view.ui_components.menus;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.designations.DesignationTypes;
import stonering.game.core.GameMvc;
import stonering.game.core.controller.controllers.DesignationsController;

/**
 * Created by Alexander on 28.05.2018.
 */
public class PlantsMenu extends ButtonMenu {
    private DesignationsController controller;

    public PlantsMenu(GameMvc gameMvc) {
        super(gameMvc, 1);
        createTable();
    }

    @Override
    public void init() {
        super.init();
        controller = gameMvc.getController().getDesignationsController();
    }

    private void createTable() {
        this.pad(10);
        this.right().bottom();

        addButtonToTable("T: chop trees", DesignationTypes.CHOP, 't');
        addButtonToTable("Z: clear", DesignationTypes.NONE, 'z');
    }

    private void addButtonToTable(String text, DesignationTypes type, char hotKey) {
        createButton(text, hotKey, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.setActiveDesignation(type);
            }
        });
    }
}
