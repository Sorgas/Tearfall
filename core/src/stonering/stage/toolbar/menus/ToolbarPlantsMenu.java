package stonering.stage.toolbar.menus;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.aspect.SelectionAspect;
import stonering.game.model.entity_selector.aspect.SelectorBoxAspect;
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
        addButtonToTable("P: chop trees", "", CHOP, Input.Keys.P);
        addButtonToTable("O: harvest", "", HARVEST, Input.Keys.O);
        addButtonToTable("I: cut", "", CUT, Input.Keys.I);
        addButtonToTable("U: clear", "", NONE, Input.Keys.U);
    }

    private void addButtonToTable(String text, String iconName, DesignationTypeEnum designationType, int hotKey) {
        createButton(text, iconName, hotKey, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                TaskContainer container = GameMvc.instance().model().get(TaskContainer.class);
                Logger.UI.logDebug("Toggling button " + text);
                SelectionAspect aspect = GameMvc.instance().model().get(EntitySelectorSystem.class).selector.getAspect(SelectionAspect.class);
                aspect.selectHandler = () ->
                        aspect.getEntity().getAspect(SelectorBoxAspect.class).boxIterator.accept(
                                position -> container.designationSystem.submitDesignation(position, designationType, 1)
                        );
            }
        }, true);
    }
}
