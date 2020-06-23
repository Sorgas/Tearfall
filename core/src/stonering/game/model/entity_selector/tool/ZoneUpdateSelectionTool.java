package stonering.game.model.entity_selector.tool;

import stonering.entity.zone.Zone;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.game.model.entity_selector.aspect.BoxSelectionAspect;
import stonering.game.model.system.ZoneContainer;
import stonering.util.geometry.Int3dBounds;

/**
 * @author Alexander on 17.03.2020
 */
public class ZoneUpdateSelectionTool extends SelectionTool {
    //TODO add validation

    @Override
    public void apply() {
        // set sprite and cursor
        EntitySelectorSystem system = GameMvc.model().get(EntitySelectorSystem.class);
        system.allowChangingZLevelOnSelection = true;
        system.allowTwoDimensionsOnSelection = true;
    }

    @Override
    public void handleSelection(Int3dBounds bounds) {
        ZoneContainer container = GameMvc.model().get(ZoneContainer.class);
        BoxSelectionAspect box = selector().get(BoxSelectionAspect.class);
        Zone zone = container.getZone(box.boxStart); // get target zone or null
        box.boxIterator.accept(position -> container.setTileToZone(zone, position));
    }
}
