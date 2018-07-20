package stonering.game.core.view.ui_components.menus;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.designations.DesignationTypes;
import stonering.game.core.GameMvc;
import stonering.game.core.controller.controllers.DesignationsController;

/**
 * @author Alexander Kuzyakov on 28.05.2018.
 */
public class PlantsMenu extends ButtonMenu {
    private DesignationsController controller;
    private PlaceSelectComponent placeSelectComponent;

    public PlantsMenu(GameMvc gameMvc) {
        super(gameMvc, true);
        hideable = true;
        initMenu();
    }

    @Override
    public void init() {
        super.init();
        controller = gameMvc.getController().getDesignationsController();
    }

    @Override
    public void reset() {
        controller.handleCancel();
    }

    private void initMenu() {
        addButtonToTable("P: chop trees", DesignationTypes.CHOP, Input.Keys.P);
        addButtonToTable("O: clear", DesignationTypes.NONE, Input.Keys.O);
        placeSelectComponent = new PlaceSelectComponent(gameMvc, false, false);
    }

    private void addButtonToTable(String text, DesignationTypes type, int hotKey) {
        createButton(text, hotKey, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.setActiveDesignation(type, null);
            }
        });
    }
}
