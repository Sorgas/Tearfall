package stonering.stage.toolbar.menus;

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
import stonering.widget.ToolbarSubMenuMenu;

/**
 * Contains buttons for designating zones. Button handlers set handlers for {@link EntitySelector}.
 * These handlers are called, when selection is committed (see ${@link EntitySelectorSystem#handleSelection()}).
 *
 * @author Alexander on 04.03.2019.
 */
public class ToolbarZonesMenu extends ToolbarSubMenuMenu {

    public ToolbarZonesMenu(Toolbar toolbar) {
        super(toolbar);
    }

    @Override
    public void init() {
        createButtons();
        super.init();
    }

    private void createButtons() {
        for (ZoneTypesEnum type : ZoneTypesEnum.values()) {
            addItem(type.toString(), type.iconName, new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    GameModel model = GameMvc.instance().model();
                    EntitySelector selector = model.get(EntitySelectorSystem.class).selector;
                    ZoneContainer container = model.get(ZoneContainer.class);
                    SelectionAspect aspect = selector.getAspect(SelectionAspect.class);
                    aspect.validator = type.validator;
                    aspect.selectHandler = box -> {
                        Zone zone = container.createZone(selector.position, type);
                        aspect.boxIterator.accept(position -> container.setTileToZone(zone, position));
                    };
                }
            }, null);
        }

        addItem("Update zone", "update_zone", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameModel model = GameMvc.instance().model();
                EntitySelector selector = model.get(EntitySelectorSystem.class).selector;
                ZoneContainer container = model.get(ZoneContainer.class);
                SelectionAspect aspect = selector.getAspect(SelectionAspect.class);
                aspect.selectHandler = box -> {
                    Zone zone = container.getZone(aspect.boxStart); // get target zone or null
                    aspect.validator = zone != null ? zone.getType().validator : position -> true; // set validator
                    aspect.boxIterator.accept(position -> container.setTileToZone(zone, position));
                };
            }
        }, null);
    }
}
