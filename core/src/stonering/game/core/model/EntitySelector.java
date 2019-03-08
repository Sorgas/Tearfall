package stonering.game.core.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import stonering.entity.local.building.validators.PositionValidator;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.core.GameMvc;
import stonering.game.core.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.util.global.Initable;

/**
 * Object selector and center for rendering.
 * Selector can have additional sprite to indicate whether position suits for building or not.
 * If positionValidator exists, position will be validated on move, updating sprite.
 * <p>
 * //TODO divide entity selecting and rendering (change this to selector, add camera class).
 * //TODO add binds for entities and positions (numbers prob.).
 * @author Alexander Kuzyakov on 10.12.2017.
 */
public class EntitySelector implements ModelComponent, Initable {
    private LocalMap localMap;
    private Position position;
    private TextureRegion selectorSprite; // shows selector position, and selected designation.
    private TextureRegion statusSprite;   // indicates position validity.

    private PositionValidator positionValidator;
    private int status;
    public static int INACTIVE_STATUS = -1;
    public static int GREEN_STATUS = 0;
    public static int RED_STATUS = 1;

    private Position frameStart; // if not null, frame from start to current position is drawn

    @Override
    public void init() {
        localMap = GameMvc.getInstance().getModel().get(LocalMap.class);
        selectorSprite = new TextureRegion(new Texture("sprites/ui_tiles.png"), 0, 406, 64, 96);
        placeToCenter();
    }

    /**
     * Places selector to the center of local map.
     */
    private void placeToCenter() {
        position = new Position(localMap.getxSize() / 2, localMap.getySize() / 2, localMap.getzSize() - 1);
        while (localMap.getBlockType(position.getX(), position.getY(), position.getZ()) <= BlockTypesEnum.SPACE.CODE) {
            position.setZ(position.getZ() - 1);
        }
    }

    public void moveSelector(int dx, int dy, int dz) {
        position.setX(position.getX() + dx);
        position.setY(position.getY() + dy);
        position.setZ(position.getZ() + dz);
        localMap.normalizePosition(position);
        updateStatusAndSprite();
    }

    /**
     * Tries to update status sprite by position validator.
     */
    public void updateStatusAndSprite() {
        if (positionValidator != null) {
            int valid = positionValidator.validate(localMap, position) ? GREEN_STATUS : RED_STATUS;
            if (valid != status) {
                status = valid;
                statusSprite = getSpriteByStatus(status);
            }
        } else {
            status = INACTIVE_STATUS;
            statusSprite = null;
        }
    }

    private TextureRegion getSpriteByStatus(int status) {
        return new TextureRegion(new Texture("sprites/ui_tiles.png"), status * 64, 567, 64, 96);
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
