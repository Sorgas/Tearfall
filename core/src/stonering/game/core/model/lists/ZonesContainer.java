package stonering.game.core.model.lists;

import stonering.entity.local.zone.ZoneActor;
import stonering.enums.ZoneTypesEnum;
import stonering.game.core.model.ModelComponent;
import stonering.util.geometry.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains and manages all zones(farms, stocks).
 */
public class ZonesContainer implements ModelComponent {
    private List<ZoneActor> zones;

    public ZonesContainer() {
        zones = new ArrayList<>();
    }

    public void createNewZone(Position pos1, Position pos2, ZoneTypesEnum type) {
        zones.add(type.createZoneActor(pos1, pos2));
    }
}
