package stonering.enums;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import stonering.entity.zone.StorageZone;
import stonering.util.validation.FarmValidator;
import stonering.util.validation.FreeFloorValidator;
import stonering.util.validation.PositionValidator;
import stonering.entity.zone.FarmZone;
import stonering.entity.zone.Zone;

/**
 * Enum for all zone types. Zone Actors are also created here.
 * Zone types hotkeys should be unique and not same as in {@link stonering.stage.toolbar.menus.ToolbarZonesMenu}.
 *
 * @author Alexander on 04.03.2019.
 */
public enum ZoneTypesEnum {
    FARM(new FarmValidator(), new TextureRegion(new Texture("sprites/zones.png"), 0, 70, 64, 96), "farm icon", Input.Keys.Y),
    STORAGE(new FreeFloorValidator(), new TextureRegion(new Texture("sprites/zones.png"), 0, 70, 64, 96), "storage_icon", Input.Keys.P);

    public final PositionValidator VALIDATOR;
    public final TextureRegion SPRITE;
    public final String ICON_NAME;
    public final int HOTKEY;

    ZoneTypesEnum(PositionValidator validator, TextureRegion sprite, String iconName, int hotKey) {
        VALIDATOR = validator;
        SPRITE = sprite;
        ICON_NAME = iconName;
        HOTKEY = hotKey;
    }

    /**
     * Creates new zone including all valid tiles into it.
     */
    public Zone createZone() {
        switch (this) {
            case FARM:
                return new FarmZone(name());
            case STORAGE:
                return new StorageZone(name());
            default:
                return null;
        }
    }
}
