package stonering.game.model.system;

import com.sun.istack.Nullable;

import stonering.entity.zone.aspect.FarmAspect;
import stonering.game.model.system.zone.FarmZoneSystem;
import stonering.game.model.system.zone.FarmTileWitherSystem;
import stonering.util.logging.Logger;
import stonering.entity.zone.Zone;
import stonering.enums.ZoneTypeEnum;
import stonering.util.geometry.Position;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Contains and manages all zones(farms, stocks). Maps positions to zones they are belong to.
 * <p>
 * Each tile can belong to one zone. //TODO give multiple zones to tiles.
 */
public class ZoneContainer extends EntityContainer<Zone> {
    private final Map<Position, Zone> zoneMap = new HashMap<>();
    
    public ZoneContainer() {
        addSystem(new FarmTileWitherSystem());
        addSystem(new FarmZoneSystem(this));
    }

    /**
     * Creates zone with single tile.
     */
    public Zone createZone(Position position, ZoneTypeEnum type) {
        Zone zone = new Zone(generateZoneName(type), type);
        createZoneAspects(zone);
        setTileToZone(zone, position);
        objects.add(zone);
        Logger.ZONES.logDebug("Zone " + zone + " created");
        recountZones();
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
    
    private void createZoneAspects(Zone zone) {
        switch(zone.type) {
            case FARM:
                zone.add(new FarmAspect());
                break;
            case STORAGE:
                break;
        }
    }
    
    private String generateZoneName(ZoneTypeEnum type) {
        for (int i = 0; i < 300; i++) {
            String name = type.name() + i;
            if(objects.stream().noneMatch(zone -> zone.name.equals(name))) return name;
        }
        return "qwer";
    }
}
