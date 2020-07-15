package stonering.enums;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import stonering.util.validation.FarmPositionValidator;
import stonering.util.validation.FreeFloorValidator;
import stonering.util.validation.PositionValidator;

/**
 * Enum for all zone types. Zone Actors are also created here.
 * Zone types hotkeys should be unique and not same as in {@link stonering.stage.toolbar.menus.ToolbarZonesMenu}.
 *
 * @author Alexander on 04.03.2019.
 */
public enum ZoneTypeEnum {
    FARM(new FarmPositionValidator(),
            new TextureRegion(new Texture("sprites/zones.png"), 0, 0, 64, 70),
            "farm icon",
            Input.Keys.Y),
    STORAGE(new FreeFloorValidator(),
            new TextureRegion(new Texture("sprites/zones.png"), 0, 0, 64, 70),
            "storage_icon",
            Input.Keys.P);

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
}
