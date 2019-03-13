package stonering.game.core.view.render.ui.menus.toolbar;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.core.GameMvc;
import stonering.game.core.controller.controllers.designation.DesignationSequence;
import stonering.game.core.controller.controllers.designation.SimpleDesignationSequence;
import stonering.game.core.controller.controllers.toolbar.DesignationsController;
import stonering.game.core.view.render.ui.menus.util.SubMenuMenu;

import static stonering.enums.designations.DesignationTypeEnum.*;

/**
 * ButtonMenu for selecting designation type.
 * Opens screen for selecting place.
 *
 * @author Alexander Kuzyakov
 */
public class DiggingMenu extends SubMenuMenu {
    private DesignationsController controller;

    public DiggingMenu() {
        hideable = true;
        initMenu();
    }

    public void init() {
        super.init();
        controller = gameMvc.getController().getDesignationsController();
    }

    private void initMenu() {
        addButton("P: dig", new SimpleDesignationSequence(DIG), Input.Keys.P);
        addButton("O: ramp", new SimpleDesignationSequence(RAMP), Input.Keys.O);
        addButton("I: channel", new SimpleDesignationSequence(CHANNEL), Input.Keys.I);
        addButton("U: stairs", new SimpleDesignationSequence(STAIRS), Input.Keys.U);
        addButton("Y: clear", new SimpleDesignationSequence(NONE), Input.Keys.Y);
    }

    private void addButton(String text, DesignationSequence sequence, int hotKey) {
        super.createButton(text, hotKey, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.setActiveDesignation(sequence); //no buildings here
                controller.startSequence();
            }
        }, true);
    }

    @Override
    public void reset() {
        controller.handleCancel();
    }
}
