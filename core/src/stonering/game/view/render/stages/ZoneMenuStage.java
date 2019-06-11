package stonering.game.view.render.stages;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.utils.Align;
import stonering.entity.local.zone.FarmZone;
import stonering.entity.local.zone.Zone;
import stonering.game.model.EntitySelector;
import stonering.game.view.render.ui.menus.zone.FarmZoneMenu;

/**
 * Stage for showing menu for selected zone.
 * This stage is shown when player selects zone tile with {@link EntitySelector}.
 * After creation this stage creates menu corresponding to zone type, shows and focuses it for keyboard input.
 *
 * @author Alexander on 20.03.2019.
 */
public class ZoneMenuStage extends UiStage {
    private Zone zone;
    private FarmZoneMenu menu;

    public ZoneMenuStage(Zone zone) {
        this.zone = zone;
        createMenu();
    }

    private void createMenu() {
        if(zone instanceof FarmZone) {
            menu = new FarmZoneMenu((FarmZone) zone);
            menu.align(Align.center);
            Container container = new Container(menu).center();
            container.setFillParent(true);
            container.setDebug(true, true);
            this.addActor(container);
            this.setKeyboardFocus(menu);
        }
    }
}
