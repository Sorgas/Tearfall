package stonering.stage.toolbar.menus;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.GameMvc;
import stonering.game.controller.controllers.designation.BoxDesignationSequence;
import stonering.game.controller.controllers.toolbar.DesignationsController;
import stonering.widget.ToolbarSubMenuMenu;

import static stonering.enums.designations.DesignationTypeEnum.*;

/**
 * ButtonMenu for selecting designation type.
 * Starts sequence for selecting place.
 *
 * @author Alexander Kuzyakov
 */
public class ToolbarDiggingMenu extends ToolbarSubMenuMenu {

    public ToolbarDiggingMenu(Toolbar toolbar) {
        super(toolbar);
        addButton("Y: dig", DIG, Input.Keys.Y);
        addButton("U: ramp", RAMP, Input.Keys.U);
        addButton("I: channel", CHANNEL, Input.Keys.I);
        addButton("H: stairs", STAIRS, Input.Keys.H); // other types of stairs are handled automatically
        addButton("K: downstairs", DOWNSTAIRS, Input.Keys.J);
        addButton("N: clear", NONE, Input.Keys.N);
    }

    private void addButton(String text, DesignationTypeEnum type, int hotKey) {
        super.createButton(text, hotKey, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                DesignationsController controller = GameMvc.instance().getController().designationsController;
                controller.setSequence(new BoxDesignationSequence(type)); //no buildings here
                controller.startSequence();
            }
        }, true);
    }

    @Override
    protected void onHide() {
        GameMvc.instance().getController().designationsController.handleCancel();
    }
}
