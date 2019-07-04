package stonering.game.model.lists;

import stonering.util.global.Logger;
import stonering.util.validation.PositionValidator;
import stonering.entity.zone.Zone;
import stonering.enums.ZoneTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.Turnable;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

import java.util.*;

/**
 * Contains and manages all zones(farms, stocks).
 * <p>
 * Each tile can belong to one zone. //TODO give multiple zones to tiles.
 */
public class ZonesContainer extends Turnable implements ModelComponent {
    private List<Zone> zones;
    private Map<Position, Zone> zoneMap;

    public ZonesContainer() {
        zones = new ArrayList<>();
        zoneMap = new HashMap<>();
    }

    @Override
    public void turn() {
        if(zones.isEmpty()) return;
        zones.forEach(Zone::turn);
    }

    /**
     * Creates new zone with given type and tiles between given positions.
     */
    public void createNewZone(Position pos1, Position pos2, ZoneTypesEnum type) {
        Zone zone = type.createZone();
        addPositionsToZone(pos1, pos2, zone);
        zones.add(zone);
        Logger.ZONES.logDebug("Zone " + zone + " created");
        recountZones();
    }

    public void deleteZone(Zone zone) {
        zone.getTiles().forEach(position -> zoneMap.remove(position));
        zones.remove(zone);
    }

    private void addPositionsToZone(Position pos1, Position pos2, Zone zone) {
        Position cachePos = new Position(0, 0, 0);
        LocalMap localMap = GameMvc.instance().getModel().get(LocalMap.class);
        PositionValidator validator = zone.getType().getValidator();
        for (cachePos.x = Math.min(pos1.x, pos2.x); cachePos.x <= Math.max(pos1.x, pos2.x); cachePos.x++) {
            for (cachePos.y = Math.min(pos1.y, pos2.y); cachePos.y <= Math.max(pos1.y, pos2.y); cachePos.y++) {
                for (cachePos.z = Math.min(pos1.z, pos2.z); cachePos.z <= Math.max(pos1.z, pos2.z); cachePos.z++) {
                    if (validator.validate(localMap, cachePos)) {
                        zone.getTiles().add(cachePos.clone());
                        zoneMap.put(cachePos.clone(), zone);
                    }
                }
            }
        }
        recountZones();
    }

    /**
     * Sets all tiles between positions to given zone.
     * Tiles, invalid for new zone will be fried too.
     */
    public void updateZones(Position pos1, Position pos2, Zone zone) {
        Position cachePos = new Position(0, 0, 0);
        LocalMap localMap = GameMvc.instance().getModel().get(LocalMap.class);
        if (zone != null) {
            for (cachePos.x = Math.min(pos1.x, pos2.x); cachePos.x <= Math.max(pos1.x, pos2.x); cachePos.x++) {
                for (cachePos.y = Math.min(pos1.y, pos2.y); cachePos.y <= Math.max(pos1.y, pos2.y); cachePos.y++) {
                    for (cachePos.z = Math.min(pos1.z, pos2.z); cachePos.z <= Math.max(pos1.z, pos2.z); cachePos.z++) {
                        if (zone.getType().getValidator().validate(localMap, cachePos)) setTile(cachePos.clone(), zone);
                    }
                }
            }
        } else {
            for (cachePos.x = Math.min(pos1.x, pos2.x); cachePos.x <= Math.max(pos1.x, pos2.x); cachePos.x++) {
                for (cachePos.y = Math.min(pos1.y, pos2.y); cachePos.y <= Math.max(pos1.y, pos2.y); cachePos.y++) {
                    for (cachePos.z = Math.min(pos1.z, pos2.z); cachePos.z <= Math.max(pos1.z, pos2.z); cachePos.z++) {
                        freeTile(cachePos);
                    }
                }
            }
        }
        Logger.ZONES.logDebug("Zone " + zone + " updated");
        recountZones();
    }

    private void recountZones() {
        for(Iterator<Zone> iterator = zones.iterator(); iterator.hasNext();) {
            Zone zone = iterator.next();
            if(!zone.getTiles().isEmpty()) continue;
            iterator.remove();
            Logger.ZONES.logDebug("Zone " + zone + " deleted");
        }
    }

    /**
     * Frees tile from any zones.
     * @param pos
     */
    public void freeTile(Position pos) {
        Zone zone = zoneMap.remove(pos);
        if (zone != null) zone.getTiles().remove(pos);
    }

    private void setTile(Position pos, Zone zone) {
        freeTile(pos);
        zoneMap.put(pos, zone);
        zone.getTiles().add(pos);
    }

    public Zone getZone(Position pos) {
        return zoneMap.get(pos);
    }
}
