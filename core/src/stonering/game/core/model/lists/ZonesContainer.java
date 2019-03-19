package stonering.game.core.model.lists;

import stonering.entity.local.building.validators.PositionValidator;
import stonering.entity.local.zone.Zone;
import stonering.enums.ZoneTypesEnum;
import stonering.game.core.GameMvc;
import stonering.game.core.model.ModelComponent;
import stonering.game.core.model.local_map.LocalMap;
import stonering.util.geometry.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains and manages all zones(farms, stocks).
 * <p>
 * Each tile can belong to one zone. //TODO give multiple zones to tiles.
 */
public class ZonesContainer implements ModelComponent {
    private List<Zone> zones;
    private Map<Position, Zone> zoneMap;

    public ZonesContainer() {
        zones = new ArrayList<>();
        zoneMap = new HashMap<>();
    }

    public void createNewZone(Position pos1, Position pos2, ZoneTypesEnum type) {
        Zone zone = type.createZone();
        addPositionsToZone(pos1, pos2, zone);
        zones.add(zone);
    }

    public Zone getZoneActor(Position pos) {
        return zoneMap.get(pos);
    }

    private void addPositionsToZone(Position pos1, Position pos2, Zone zone) {
        Position cachePos = new Position(0, 0, 0);
        LocalMap localMap = GameMvc.getInstance().getModel().get(LocalMap.class);
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
    }

    /**
     * Sets all tiles between positions to given zone.
     * Tiles, invalid for new zone will be fried too.
     */
    public void updateZones(Position pos1, Position pos2, Zone zone) {
        Position cachePos = new Position(0, 0, 0);
        LocalMap localMap = GameMvc.getInstance().getModel().get(LocalMap.class);
        if (zone != null) {
            for (cachePos.x = Math.min(pos1.x, pos2.x); cachePos.x <= Math.max(pos1.x, pos2.x); cachePos.x++) {
                for (cachePos.y = Math.min(pos1.y, pos2.y); cachePos.y <= Math.max(pos1.y, pos2.y); cachePos.y++) {
                    for (cachePos.z = Math.min(pos1.z, pos2.z); cachePos.z <= Math.max(pos1.z, pos2.z); cachePos.z++) {
                        if (zone.getType().getValidator().validate(localMap, cachePos)) setTile(cachePos, zone);
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
    }

    public void freeTile(Position pos) {
        Zone zone = zoneMap.remove(pos);
        if (zone != null) zone.getTiles().remove(pos);
    }

    public void setTile(Position pos, Zone zone) {
        freeTile(pos);
        zoneMap.put(pos, zone);
        zone.getTiles().add(pos);
    }
}
