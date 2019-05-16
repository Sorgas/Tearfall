package stonering.game.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import stonering.util.global.Startable;
import stonering.util.validation.PositionValidator;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
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
public class EntitySelector implements ModelComponent, Startable {
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

//    @Override
//    public void init() {
//
//    }

    /**
     * Places selector to the center of local map.
     */
    @Override
    public void start() {
        localMap = GameMvc.instance().getModel().get(LocalMap.class);
        selectorSprite = new TextureRegion(new Texture("sprites/ui_tiles.png"), 0, 406, 64, 96);
        position = new Position(localMap.xSize / 2, localMap.ySize / 2, localMap.zSize - 1);
        while (localMap.getBlockType(position.x, position.y, position.z) == BlockTypesEnum.SPACE.CODE) {
            position.z--;
        }
    }

    public void moveSelector(int dx, int dy, int dz) {
        position.setX(position.getX() + dx);
        position.setY(position.getY() + dy);
        position.setZ(position.getZ() + dz);
        localMap.normalizePosition(position);
        updateStatusAndSprite();
        GameMvc.instance().getView().updateDrawableArea();
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
        updateStatusAndSprite();
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
