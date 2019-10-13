package stonering.stage.toolbar.menus;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.GameMvc;
import stonering.game.controller.controllers.designation.BoxDesignationSequence;
import stonering.game.controller.controllers.toolbar.DesignationsController;
import stonering.widget.ToolbarSubMenuMenu;
import stonering.util.global.Logger;

import static stonering.enums.designations.DesignationTypeEnum.*;

/**
 * Menu with orders related to plants
 *
 * @author Alexander Kuzyakov on 28.05.2018.
 */
public class PlantsMenu extends ToolbarSubMenuMenu {

    public PlantsMenu(Toolbar toolbar) {
        super(toolbar);
        initMenu();
    }

    @Override
    public void init() {
        super.init();
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
                Logger.UI.logDebug("Toggling button " + text);
                DesignationsController controller = GameMvc.instance().getController().designationsController;
                controller.setSequence(new BoxDesignationSequence(designationType));
                controller.startSequence();
            }
        }, true);
    }

    @Override
    protected void onHide() {
        GameMvc.instance().getController().designationsController.handleCancel();
    }
}
