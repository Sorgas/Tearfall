package stonering.enums;

import stonering.entity.local.building.validators.FreeSoilFloorValidator;
import stonering.entity.local.building.validators.PositionValidator;
import stonering.entity.local.zone.FarmZone;
import stonering.entity.local.zone.Zone;

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

    /**
     * Creates new zone including all valid tiles into it.
     */
    public Zone createZone() {
        switch (this) {
            case FARM:
                return new FarmZone(name());
            default:
                return null;
        }
    }

    public PositionValidator getValidator() {
        return validator;
    }
}
