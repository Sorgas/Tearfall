package stonering.stage.toolbar.menus;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.controller.controllers.designation.BoxDesignationSequence;
import stonering.widget.ToolbarSubMenuMenu;

import static stonering.enums.designations.DesignationTypeEnum.*;

/**
 * ButtonMenu for selecting designation type.
 * When this menu is shown, designation type is set to dig. Player can designate digging, or change designation type.
 * If designation type is changed during area selection, area is updated with rules of new designation.
 * Starts sequence for selecting place.
 *
 * @author Alexander Kuzyakov
 */
public class ToolbarDiggingMenu extends ToolbarSubMenuMenu {
    private BoxDesignationSequence sequence;

    public ToolbarDiggingMenu(Toolbar toolbar) {
        super(toolbar);
        addButton("Y: dig", DIG, Input.Keys.Y);
        addButton("U: ramp", RAMP, Input.Keys.U);
        addButton("I: channel", CHANNEL, Input.Keys.I);
        addButton("H: stairs", STAIRS, Input.Keys.H); // other types of stairs are handled automatically
        addButton("K: downstairs", DOWNSTAIRS, Input.Keys.J);
        addButton("N: clear", NONE, Input.Keys.N);
        sequence = new BoxDesignationSequence(DIG);
    }

    private void addButton(String text, DesignationTypeEnum type, int hotKey) {
        super.createButton(text, hotKey, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sequence.designationType = type;

            }
        }, true);
    }

    @Override
    public void show() {
        super.show();
        sequence.designationType = DIG; // DIG is default designation
        sequence.start();
    }

    @Override
    protected void onHide() {
        sequence.reset();
    }
}
