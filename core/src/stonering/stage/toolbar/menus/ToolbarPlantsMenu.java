package stonering.stage.toolbar.menus;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.aspect.SelectionAspect;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.game.model.entity_selector.tool.SelectionTools;
import stonering.stage.toolbar.Toolbar;
import stonering.widget.ToolbarSubMenuMenu;

import static stonering.enums.designations.DesignationTypeEnum.*;

/**
 * Menu with orders related to plants.
 *
 * @author Alexander Kuzyakov on 28.05.2018.
 */
public class ToolbarPlantsMenu extends ToolbarSubMenuMenu {

    public ToolbarPlantsMenu(Toolbar toolbar) {
        super(toolbar);
        initMenu();
    }

    private void initMenu() {
        addButtonToTable("P: chop trees", "", D_CHOP, Input.Keys.P);
        addButtonToTable("O: harvest", "", D_HARVEST, Input.Keys.O);
        addButtonToTable("I: cut", "", D_CUT, Input.Keys.I);
        addButtonToTable("U: clear", "", D_NONE, Input.Keys.U);
    }

    private void addButtonToTable(String text, String iconName, DesignationTypeEnum type, int hotKey) {
        createButton(text, iconName, hotKey, () -> {
            SelectionTools.DESIGNATION.setType(type);
            GameMvc.model().get(EntitySelectorSystem.class).selector.get(SelectionAspect.class).set(SelectionTools.DESIGNATION);
        });
    }
}
