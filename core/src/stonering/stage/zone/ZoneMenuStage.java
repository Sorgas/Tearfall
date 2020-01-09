package stonering.stage.zone;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.utils.Align;
import stonering.entity.zone.FarmZone;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.stage.UiStage;

/**
 * Stage for showing menu for selected zone.
 * This stage is shown when player selects zone tile with {@link EntitySelector}.
 * After creation this stage creates menu corresponding to zone type, shows and focuses it for keyboard input.
 *
 * @author Alexander on 20.03.2019.
 */
public class ZoneMenuStage extends UiStage {
    private FarmZoneMenu menu;

    public ZoneMenuStage(FarmZone zone) {
        createMenu(zone);
        interceptInput = true;
    }

    private void createMenu(FarmZone zone) {
        menu = new FarmZoneMenu(zone);
        menu.align(Align.center);
        Container container = new Container(menu).center();
        container.setFillParent(true);
        container.setDebug(true, true);
        this.addActor(container);
        this.setKeyboardFocus(menu.disabledPlants);
    }
}
