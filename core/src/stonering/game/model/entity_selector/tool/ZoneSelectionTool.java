package stonering.game.model.entity_selector.tool;

import stonering.entity.zone.Zone;
import stonering.enums.ZoneTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.game.model.entity_selector.aspect.BoxSelectionAspect;
import stonering.game.model.system.ZoneContainer;
import stonering.util.geometry.Int3dBounds;

/**
 * Tool sets selected tile to new zone of specified type.
 *
 * @author Alexander on 17.03.2020
 */
public class ZoneSelectionTool extends SelectionTool {
    public ZoneTypesEnum type;
    // TODO add validation

    @Override
    public void apply() {
        // set selector sprite and cursor
        selector().size.set(1, 1);
        EntitySelectorSystem system = GameMvc.model().get(EntitySelectorSystem.class);
        system.allowChangingZLevelOnSelection = true;
        system.allowTwoDimensionsOnSelection = true;
    }

    @Override
    public void handleSelection(Int3dBounds bounds) {
        ZoneContainer container = GameMvc.model().get(ZoneContainer.class);
        BoxSelectionAspect box = selector().get(BoxSelectionAspect.class);
        Zone zone = container.createZone(selector().position, type);
        box.boxIterator.accept(position -> container.setTileToZone(zone, position));
    }
}
