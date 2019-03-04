package stonering.enums;

import stonering.entity.local.zone.FarmZone;
import stonering.entity.local.zone.ZoneActor;

/**
 * @author Alexander on 04.03.2019.
 */
public enum ZoneTypesEnum {
    FARM(FarmZone.class);

    private Class<? extends ZoneActor> zoneClass;

    ZoneTypesEnum(Class<? extends ZoneActor> zoneClass) {
        this.zoneClass = zoneClass;
    }

    public Class<? extends ZoneActor> getZoneClass() {
        return zoneClass;
    }
}
