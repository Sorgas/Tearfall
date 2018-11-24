package stonering.game.core.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import stonering.entity.local.building.validators.PositionValidator;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.global.utils.Position;

/**
 * Object selector and center for rendering.
 * Selector can have additional sprite to indicate whether position suits for building or not.
 * If positionValidator exists, position will be validated on move, updating sprite.
 * <p>
 * //TODO divide entity selecting and rendering (change this to selector, add camera class).
 *
 * @author Alexander Kuzyakov on 10.12.2017.
 */
public class EntitySelector {
    private GameContainer container;
    private LocalMap localMap;
    private Position position;
    private TextureRegion selectorSprite; // shows selector position, and selected designation.
    private TextureRegion statusSprite;   // indicates position validity.

    private PositionValidator positionValidator;
    int status;
    public static int INACTIVE_STATUS = -1;
    public static int GREEN_STATUS = 0;
    public static int RED_STATUS = 1;

    private Position frameStart; // if not null, frame from start to current position is drawn

    public EntitySelector(GameContainer container) {
        this.container = container;
        localMap = container.getLocalMap();
        initSelector();
    }

    private void initSelector() {
        position = new Position(localMap.getxSize() / 2, localMap.getySize() / 2, localMap.getzSize() - 1);
        while (localMap.getBlockType(position.getX(), position.getY(), position.getZ()) <= BlockTypesEnum.SPACE.getCode()) {
            position.setZ(position.getZ() - 1);
        }
        selectorSprite = new TextureRegion(new Texture("sprites/ui_tiles.png"), 0, 406, 64, 96);
    }

    public void moveSelector(int dx, int dy, int dz) {
        position.setX(position.getX() + dx);
        position.setY(position.getY() + dy);
        position.setZ(position.getZ() + dz);
        localMap.normalizePosition(position);
        updateStatus();
    }

    /**
     * Tries to update status sprite by position validator.
     */
    public void updateStatus() {
        if (positionValidator != null) {
            int valid = positionValidator.validate(localMap, position) ? GREEN_STATUS : RED_STATUS;
            if (valid != status) {
                status = valid;
                statusSprite = new TextureRegion(new Texture("sprites/ui_tiles.png"), status * 64, 567, 64, 96);
            }
        } else {
            status = INACTIVE_STATUS;
            statusSprite = null;
        }
    }

    public Position getFrameStart() {
        return frameStart;
    }

    public void setFrameStart(Position frameStart) {
        this.frameStart = frameStart;
    }

    public TextureRegion getSelectorSprite() {
        return selectorSprite;
    }

    public void setPositionValidator(PositionValidator positionValidator) {
        this.positionValidator = positionValidator;
    }

    public Position getPosition() {
        return position;
    }

    public int getStatus() {
        return status;
    }

    public TextureRegion getStatusSprite() {
        return statusSprite;
    }
}
