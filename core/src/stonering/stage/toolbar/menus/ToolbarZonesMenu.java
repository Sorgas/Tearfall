package stonering.stage.toolbar.menus;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.entity.zone.Zone;
import stonering.enums.ZoneTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.game.model.entity_selector.aspect.SelectionAspect;
import stonering.game.model.system.EntitySelectorSystem;
import stonering.game.model.system.ZoneContainer;
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
        createButton("Update zone", Input.Keys.U, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameModel model = GameMvc.model();
                EntitySelector selector = model.get(EntitySelectorSystem.class).selector;
                ZoneContainer container = model.get(ZoneContainer.class);
                SelectionAspect aspect = selector.getAspect(SelectionAspect.class);
                aspect.selectHandler = box -> {
                    Zone zone = container.getZone(aspect.boxStart); // get target zone or null
                    aspect.validator = zone != null ? zone.getType().VALIDATOR : position -> true; // set validator if zone is extended
                    aspect.boxIterator.accept(position -> container.setTileToZone(zone, position));
                };
            }
        }, true);
    }

    private void createButtonForZone(ZoneTypesEnum type) {
        super.createButton(type.toString(), type.HOTKEY, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameModel model = GameMvc.model();
                EntitySelector selector = model.get(EntitySelectorSystem.class).selector;
                ZoneContainer container = model.get(ZoneContainer.class);
                SelectionAspect aspect = selector.getAspect(SelectionAspect.class);
                aspect.validator = type.VALIDATOR;
                aspect.selectHandler = box -> {
                    Zone zone = container.createZone(selector.position, type);
                    aspect.boxIterator.accept(position -> container.setTileToZone(zone, position));
                };
            }
        }, true);
    }
}
