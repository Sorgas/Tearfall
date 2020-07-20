package stonering.enums;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import stonering.util.validation.zone.farm.FarmPositionValidator;
import stonering.util.validation.FreeFloorValidator;
import stonering.util.validation.PositionValidator;

/**
 * Enum for all zone types.
 * Zone types hotkeys should be unique and not same as in {@link stonering.stage.toolbar.menus.ToolbarZonesMenu}.
 *
 * @author Alexander on 04.03.2019.
 */
public enum ZoneTypeEnum {
    FARM(new FarmPositionValidator(), "farm_icon", Input.Keys.Y),
    STORAGE(new FreeFloorValidator(), "storage_icon", Input.Keys.P);

    public final PositionValidator VALIDATOR;
    public final TextureRegion SPRITE;
    public final String ICON_NAME;
    public final int HOTKEY;

    ZoneTypeEnum(PositionValidator validator, TextureRegion sprite, String iconName, int hotKey) {
        VALIDATOR = validator;
        SPRITE = sprite;
        ICON_NAME = iconName;
        HOTKEY = hotKey;
    }

    ZoneTypeEnum(PositionValidator validator, String iconName, int hotKey) {
        this(validator, new TextureRegion(new Texture("sprites/zones.png"), 0, 0, 64, 70), iconName, hotKey);
    }
}
