package stonering.stage.toolbar.menus;

import com.badlogic.gdx.Input;

import stonering.enums.ZoneTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.game.model.entity_selector.aspect.SelectionAspect;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.game.model.entity_selector.tool.SelectionTools;
import stonering.stage.toolbar.Toolbar;
import stonering.widget.ToolbarButtonMenu;

import java.util.Arrays;

/**
 * Contains buttons for designating zones. Button handlers set handlers for {@link EntitySelector}.
 * These handlers are called, when selection is committed (see ${@link EntitySelectorSystem#handleSelection()}).
 *
 * @author Alexander on 04.03.2019.
 */
public class ToolbarZonesMenu extends ToolbarButtonMenu {

    public ToolbarZonesMenu(Toolbar toolbar) {
        super(toolbar);
        createButtons();
    }

    private void createButtons() {
        Arrays.stream(ZoneTypesEnum.values()).forEach(this::createButtonForZone);
        createButton("Update zone", Input.Keys.U, () -> {
            GameMvc.model().get(EntitySelectorSystem.class).selector.get(SelectionAspect.class).set(SelectionTools.ZONE_UPDATE);
        }, true);
    }

    private void createButtonForZone(ZoneTypesEnum type) {
        super.createButton(type.toString(), type.HOTKEY, () -> {
            SelectionTools.ZONE.type = type;
            GameMvc.model().get(EntitySelectorSystem.class).selector.get(SelectionAspect.class).set(SelectionTools.ZONE);
        }, true);
    }
}
