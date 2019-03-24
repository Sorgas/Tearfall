package stonering.game.view.render.stages;

import stonering.entity.local.zone.Zone;
import stonering.game.view.render.stages.base.UiStage;

/**
 * Stage for showing
 *
 * @author Alexander on 20.03.2019.
 */
public class ZoneMenuStage extends UiStage {
    private Zone zone;

    public ZoneMenuStage(Zone zone) {
        this.zone = zone;
    }
}
