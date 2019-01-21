package stonering.game.core.view.render.ui.menus;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.game.core.GameMvc;
import stonering.game.core.controller.controllers.designation.DesignationSequence;
import stonering.game.core.controller.controllers.designation.SimpleDesignationSequence;
import stonering.game.core.controller.controllers.toolbar.DesignationsController;
import stonering.game.core.view.render.ui.menus.util.RectangleSelectComponent;
import stonering.game.core.view.render.ui.menus.util.SubMenuMenu;

import static stonering.enums.designations.DesignationTypeEnum.*;

/**
 * Menu with orders related to plants
 *
 * @author Alexander Kuzyakov on 28.05.2018.
 */
public class PlantsMenu extends SubMenuMenu {
    private DesignationsController controller;
    private RectangleSelectComponent rectangleSelectComponent;

    public PlantsMenu(GameMvc gameMvc) {
        super(gameMvc);
        hideable = true;
        initMenu();
    }

    @Override
    public void init() {
        super.init();
        controller = gameMvc.getController().getDesignationsController();
        rectangleSelectComponent.init();
    }

    @Override
    public void reset() {
        controller.handleCancel();
    }

    private void initMenu() {
        addButtonToTable("P: chop trees", new SimpleDesignationSequence(CHOP), Input.Keys.P);
        addButtonToTable("O: harvest", new SimpleDesignationSequence(HARVEST), Input.Keys.O);
        addButtonToTable("I: cut", new SimpleDesignationSequence(CUT), Input.Keys.I);
        addButtonToTable("U: clear", new SimpleDesignationSequence(NONE), Input.Keys.U);

        rectangleSelectComponent = new RectangleSelectComponent(gameMvc);
    }

    private void addButtonToTable(String text, DesignationSequence sequence, int hotKey) {
        createButton(text, hotKey, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.setActiveDesignation(sequence, null);
            }
        }, true);
    }
}
