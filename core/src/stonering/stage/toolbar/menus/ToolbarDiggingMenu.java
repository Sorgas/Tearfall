package stonering.stage.toolbar.menus;

import com.badlogic.gdx.Input;

import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.aspect.SelectionAspect;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.game.model.entity_selector.tool.SelectionTools;
import stonering.stage.toolbar.Toolbar;
import stonering.widget.ToolbarButtonMenu;

import static stonering.enums.designations.DesignationTypeEnum.*;

/**
 * ButtonMenu for selecting designation type.
 * When this menu is shown, designation type is set to dig. Player can designate digging, or change designation type.
 * If designation type is changed during area selection, area is updated with rules of new designation.
 * Starts sequence for selecting place.
 *
 * @author Alexander Kuzyakov
 */
public class ToolbarDiggingMenu extends ToolbarButtonMenu {

    public ToolbarDiggingMenu(Toolbar toolbar) {
        super(toolbar);
        addButton("Y: dig", D_DIG, Input.Keys.Y);
        addButton("U: ramp", D_RAMP, Input.Keys.U);
        addButton("I: channel", D_CHANNEL, Input.Keys.I);
        addButton("H: stairs", D_STAIRS, Input.Keys.H); // other types of stairs are handled automatically
        addButton("K: downstairs", D_DOWNSTAIRS, Input.Keys.J);
        addButton("N: clear", D_NONE, Input.Keys.N);
        addButton("Q: back", Input.Keys.Q, this::hide);
    }

    private void addButton(String text, DesignationTypeEnum type, int hotKey) {
        super.addButton(text, hotKey, () -> {
            SelectionTools.DESIGNATION.setType(type);
            GameMvc.model().get(EntitySelectorSystem.class).selector.get(SelectionAspect.class).set(SelectionTools.DESIGNATION);
        });
    }
}
