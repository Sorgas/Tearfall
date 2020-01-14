package stonering.game.model.system;

import stonering.util.global.Logger;
import stonering.util.validation.PositionValidator;
import stonering.entity.zone.Zone;
import stonering.enums.ZoneTypesEnum;
import stonering.util.geometry.Position;

import java.util.*;

/**
 * Contains and manages all zones(farms, stocks).
 * <p>
 * Each tile can belong to one zone. //TODO give multiple zones to tiles.
 */
public class ZoneContainer extends EntityContainer<Zone> {
    private Map<Position, Zone> zoneMap;

    public ZoneContainer() {
        zoneMap = new HashMap<>();
    }

    /**
     * Creates zone with single tile.
     */
    public Zone createZone(Position position, ZoneTypesEnum type) {
        Zone zone = type.createZone();
        addPositionToZone(zone, position);
        entities.add(zone);
        Logger.ZONES.logDebug("Zone " + zone + " created");
        recountZones();
        return zone;
    }

    public void deleteZone(Zone zone) {
        zone.getTiles().forEach(position -> zoneMap.remove(position));
        entities.remove(zone);
    }

    public void addPositionToZone(Zone zone, Position position) {
        PositionValidator validator = zone.getType().getValidator();
        if (validator.validate(position)) {
            zone.getTiles().add(position.clone());
            zoneMap.put(position.clone(), zone);
        }
    }

    public void updateZone(Position position, Zone zone) {
        Zone oldZone = zoneMap.remove(position);
        if (oldZone != null) zone.getTiles().remove(position);
        if (zone != null) {
            zoneMap.put(position, zone);
            zone.getTiles().add(position.clone());
        }
        Logger.ZONES.logDebug("Zone " + zone + " updated");
        recountZones();
    }

    private void recountZones() {
        for (Iterator<Zone> iterator = entities.iterator(); iterator.hasNext(); ) {
            Zone zone = iterator.next();
            if (!zone.getTiles().isEmpty()) continue;
            iterator.remove();
            Logger.ZONES.logDebug("Zone " + zone + " deleted");
        }
    }

    public Zone getZone(Position pos) {
        return zoneMap.get(pos);
    }
}
