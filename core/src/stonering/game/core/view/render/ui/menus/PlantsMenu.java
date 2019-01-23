package stonering.game.core.view.render.ui.menus;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.core.GameMvc;
import stonering.game.core.controller.controllers.designation.SimpleDesignationSequence;
import stonering.game.core.controller.controllers.toolbar.DesignationsController;
import stonering.game.core.view.render.ui.menus.util.SubMenuMenu;
import stonering.util.global.TagLoggersEnum;

import static stonering.enums.designations.DesignationTypeEnum.*;

/**
 * Menu with orders related to plants
 *
 * @author Alexander Kuzyakov on 28.05.2018.
 */
public class PlantsMenu extends SubMenuMenu {
    private DesignationsController controller;

    public PlantsMenu(GameMvc gameMvc) {
        super(gameMvc);
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
        addButtonToTable("P: chop trees", CHOP, Input.Keys.P);
        addButtonToTable("O: harvest", HARVEST, Input.Keys.O);
        addButtonToTable("I: cut", CUT, Input.Keys.I);
        addButtonToTable("U: clear", NONE, Input.Keys.U);
    }

    private void addButtonToTable(String text, DesignationTypeEnum designationType, int hotKey) {
        createButton(text, hotKey, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                event.stop();
                TagLoggersEnum.UI.logDebug("Toggling button " + text);
                controller.setActiveDesignation(new SimpleDesignationSequence(designationType));
                controller.startSequence();
            }
        }, true);
    }
}
