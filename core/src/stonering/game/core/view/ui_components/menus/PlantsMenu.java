package stonering.game.core.view.ui_components.menus;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.designations.DesignationTypes;
import stonering.game.core.GameMvc;
import stonering.game.core.controller.controllers.DesignationsController;
import stonering.utils.global.StaticSkin;

/**
 * Created by Alexander on 28.05.2018.
 */
public class PlantsMenu extends Menu {
    private DesignationsController controller;

    public PlantsMenu(GameMvc gameMvc) {
        super(gameMvc);
        createTable();
    }

    @Override
    public void init() {
        controller = gameMvc.getController().getDesignationsController();
    }

    private void createTable() {
        this.pad(10);
        this.right().bottom();

        addButtonToTable("T: chop trees", DesignationTypes.CHOP, 't');
        addButtonToTable("Z: clear", DesignationTypes.NONE, 'z');
        addButtonToTable("ESC: cancel", null, (char) 27);
    }

    private void addButtonToTable(String text, DesignationTypes type, char hotKey) {
        TextButton button = new TextButton(text, StaticSkin.getSkin());
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.setActiveDesignation(type);
//                toolbar.closeMenus();
            }
        });
        this.addActor(button);
    }
}
