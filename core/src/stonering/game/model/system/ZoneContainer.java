package stonering.game.model.system;

import com.sun.istack.Nullable;
import stonering.game.model.system.zone.FarmTileWitherSystem;
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
        putSystem(new FarmTileWitherSystem());
    }

    /**
     * Creates zone with single tile.
     */
    public Zone createZone(Position position, ZoneTypesEnum type) {
        Zone zone = type.createZone();
        setTileToZone(zone, position);
        entities.add(zone);
        Logger.ZONES.logDebug("Zone " + zone + " created");
        recountZones();
        return zone;
    }

    public void deleteZone(Zone zone) {
        zone.getTiles().forEach(position -> zoneMap.remove(position));
        entities.remove(zone);
    }

    public void setTileToZone(@Nullable Zone zone, Position position) {
        Zone oldZone = zoneMap.remove(position);
        if (oldZone != null) zone.getTiles().remove(position);
        if (zone != null) {
            PositionValidator validator = zone.getType().VALIDATOR;
            if (validator.validate(position)) {
                zone.getTiles().add(position.clone());
                zoneMap.put(position.clone(), zone);
            }
            zoneMap.put(position, zone);
            zone.getTiles().add(position.clone());
        }
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
