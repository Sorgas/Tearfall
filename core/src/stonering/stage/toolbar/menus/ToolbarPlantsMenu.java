package stonering.stage.toolbar.menus;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.aspect.SelectionAspect;
import stonering.game.model.system.EntitySelectorSystem;
import stonering.game.model.system.task.TaskContainer;
import stonering.widget.ToolbarSubMenuMenu;
import stonering.util.global.Logger;

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
        createButton(text, iconName, hotKey, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                TaskContainer container = GameMvc.model().get(TaskContainer.class);
                Logger.UI.logDebug("Toggling button " + text);
                SelectionAspect aspect = GameMvc.model().get(EntitySelectorSystem.class).selector.getAspect(SelectionAspect.class);
                aspect.validator = type.VALIDATOR;
                aspect.selectHandler = box -> aspect.boxIterator.accept(position -> container.designationSystem.submitDesignation(position, type, 1));
                aspect.cancelHandler = aspect::reset;
            }
        }, true);
    }
}
