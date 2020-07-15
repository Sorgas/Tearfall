package stonering.stage.toolbar.menus;

import com.badlogic.gdx.Input;

import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.aspect.SelectionAspect;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.game.model.entity_selector.tool.SelectionTools;
import stonering.stage.toolbar.Toolbar;
import stonering.widget.ToolbarSubmenuMenu;

import static stonering.enums.designations.DesignationTypeEnum.*;

/**
 * Menu with orders related to plants.
 *
 * @author Alexander Kuzyakov on 28.05.2018.
 */
public class ToolbarPlantsMenu extends ToolbarSubmenuMenu {

    public ToolbarPlantsMenu(Toolbar toolbar) {
        super(toolbar);
        initMenu();
    }

    private void initMenu() {
        addButtonToTable("P: chop trees", "", D_CHOP, Input.Keys.P);
        addButtonToTable("O: harvest", "", D_HARVEST, Input.Keys.O);
        addButtonToTable("I: cut", "", D_CUT, Input.Keys.I);
        addButtonToTable("U: clear", "", D_NONE, Input.Keys.U);
        addButton("Q: back", Input.Keys.Q, this::hide);
    }

    private void addButtonToTable(String text, String iconName, DesignationTypeEnum type, int hotKey) {
        addButton(text, iconName, hotKey, () -> {
            SelectionTools.DESIGNATION.setType(type);
            GameMvc.model().get(EntitySelectorSystem.class).selector.get(SelectionAspect.class).set(SelectionTools.DESIGNATION);
        });
    }
}
