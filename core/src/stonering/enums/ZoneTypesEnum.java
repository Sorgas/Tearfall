package stonering.enums;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import stonering.util.validation.FreeSoilFloorValidator;
import stonering.util.validation.PositionValidator;
import stonering.entity.local.zone.FarmZone;
import stonering.entity.local.zone.Zone;

/**
 * Enum for all zone types. Zone Actors are also created here.
 *
 * @author Alexander on 04.03.2019.
 */
public enum ZoneTypesEnum {
    FARM(new FreeSoilFloorValidator(), new TextureRegion(new Texture("sprites/zones.png"), 0, 70, 64, 96));

    private PositionValidator validator;
    public final TextureRegion sprite;

    ZoneTypesEnum(PositionValidator validator, TextureRegion sprite) {
        this.validator = validator;
        this.sprite = sprite;
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
