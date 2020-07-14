package stonering.game.model.system;

import com.sun.istack.Nullable;

import stonering.game.model.system.zone.FarmZoneSystem;
import stonering.game.model.system.zone.FarmTileWitherSystem;
import stonering.util.logging.Logger;
import stonering.entity.zone.Zone;
import stonering.enums.ZoneTypesEnum;
import stonering.util.geometry.Position;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Contains and manages all zones(farms, stocks). Maps positions to zones they are belong to.
 * <p>
 * Each tile can belong to one zone. //TODO give multiple zones to tiles.
 */
public class ZoneContainer extends EntityContainer<Zone> {
    private Map<Position, Zone> zoneMap;

    public ZoneContainer() {
        zoneMap = new HashMap<>();
        put(new FarmTileWitherSystem());
        put(new FarmZoneSystem());
    }

    /**
     * Creates zone with single tile.
     */
    public Zone createZone(Position position, ZoneTypesEnum type) {
        Zone zone = type.createZone();
        if(zone != null) {
            setTileToZone(zone, position);
            objects.add(zone);
            Logger.ZONES.logDebug("Zone " + zone + " created");
            recountZones();
        }
        return zone;
    }

    public void deleteZone(Zone zone) {
        zone.tiles.forEach(position -> zoneMap.remove(position));
        objects.remove(zone);
    }

    public void setTileToZone(@Nullable Zone zone, Position position) {
        if(zone == null) { // delete previous zone from tile
            removeTileFromZones(position);
            recountZones();
        } else if (zone.type.VALIDATOR.apply(position)) { // replace zone if tile is valid for new zone 
            removeTileFromZones(position);
            zone.tiles.add(position);
            zoneMap.put(position, zone);
            recountZones();
        }
    }

    private void removeTileFromZones(Position position) {
        Optional.ofNullable(zoneMap.remove(position)).ifPresent(zone -> zone.tiles.remove(position));
    }
    
    private void recountZones() {
        List<Zone> emptyZones = objects.stream()
                .filter(zone -> zone.tiles.isEmpty())
                .collect(Collectors.toList());
        objects.removeAll(emptyZones);
    }

    public Zone getZone(Position pos) {
        return zoneMap.get(pos);
    }
}
