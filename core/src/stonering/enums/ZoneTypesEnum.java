package stonering.enums;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import stonering.util.validation.FarmValidator;
import stonering.util.validation.FreeFloorValidator;
import stonering.util.validation.PositionValidator;
import stonering.entity.zone.FarmZone;
import stonering.entity.zone.Zone;

/**
 * Enum for all zone types. Zone Actors are also created here.
 *
 * @author Alexander on 04.03.2019.
 */
public enum ZoneTypesEnum {
    FARM(new FarmValidator(), new TextureRegion(new Texture("sprites/zones.png"), 0, 70, 64, 96), "farm icon"),
    STORAGE(new FreeFloorValidator(), new TextureRegion(new Texture("sprites/zones.png"), 0, 70, 64, 96), "storage_icon");

    private PositionValidator validator;
    public final TextureRegion sprite;
    public final String iconName;

    ZoneTypesEnum(PositionValidator validator, TextureRegion sprite, String iconName) {
        this.validator = validator;
        this.sprite = sprite;
        this.iconName = iconName;
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
