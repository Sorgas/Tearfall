package stonering.enums;

import stonering.entity.local.building.validators.FreeSoilFloorValidator;
import stonering.entity.local.building.validators.PositionValidator;
import stonering.entity.local.zone.FarmZoneActor;
import stonering.entity.local.zone.Zone;
import stonering.entity.local.zone.ZoneActor;
import stonering.util.geometry.Area;
import stonering.util.geometry.Position;

/**
 * Enum for all zone types. Zone Actors are also created here.
 *
 * @author Alexander on 04.03.2019.
 */
public enum ZoneTypesEnum {
    FARM(new FreeSoilFloorValidator());

    private PositionValidator validator;

    ZoneTypesEnum(PositionValidator validator) {
        this.validator = validator;
    }

    public ZoneActor createZoneActor(Position pos1, Position pos2) {
        switch (this) {
            case FARM:
                return new FarmZoneActor(name(), new Zone(new Area(pos1, pos2)));
            default:
                return null;
        }
    }

    public PositionValidator getValidator() {
        return validator;
    }
}
