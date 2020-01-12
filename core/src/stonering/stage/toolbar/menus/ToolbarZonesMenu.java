package stonering.stage.toolbar.menus;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.entity.zone.Zone;
import stonering.enums.ZoneTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.game.model.entity_selector.aspect.SelectionAspect;
import stonering.game.model.entity_selector.aspect.SelectorBoxAspect;
import stonering.game.model.system.EntitySelectorSystem;
import stonering.game.model.system.ZoneContainer;
import stonering.util.geometry.Position;
import stonering.widget.ToolbarSubMenuMenu;

/**
 * Contains buttons for designating zones.
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
                    selector.getAspect(SelectionAspect.class).selectHandler =
                            () -> model.get(ZoneContainer.class).createNewZone(selector.getAspect(SelectorBoxAspect.class).boxStart, selector.position.clone(), type);
                }
            }, null);
        }

        addItem("Update zone", "update_zone", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameModel model = GameMvc.instance().model();
                EntitySelector selector = model.get(EntitySelectorSystem.class).selector;
                ZoneContainer container = model.get(ZoneContainer.class);
                selector.getAspect(SelectionAspect.class).selectHandler = () -> {
                    Position boxStart = selector.getAspect(SelectorBoxAspect.class).boxStart;
                    Zone zone = container.getZone(boxStart); // tiles in selected area will get this zone, and removed from previous.
                    container.updateZones(boxStart, selector.position.clone(), zone);
                };

            }
        }, null);
    }
}
