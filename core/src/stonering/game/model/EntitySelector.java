package stonering.game.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.model.system.ModelComponent;
import stonering.util.validation.PositionValidator;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.util.global.Initable;

/**
 * Selects objects on local map. Moved by mouse or WASDRF. When is moved by mouse sprite is not shown
 * Selector can have additional sprite to indicate whether position suits for building or not.
 * If positionValidator exists, position will be validated on move, updating sprite.
 * <p>
 * //TODO divide entity selecting and rendering (change this to selector, add camera class).
 * //TODO add binds for entities and positions (numbers prob.).
 *
 * @author Alexander Kuzyakov on 10.12.2017.
 */
public class EntitySelector implements ModelComponent, Initable {
    private LocalMap localMap;
    public final Position position;
    private TextureRegion selectorSprite; // shows selector position, and selected designation.
    private TextureRegion statusSprite;   // indicates position validity.
    private Position cachePosition;

    private PositionValidator positionValidator;
    private int status;
    public static int INACTIVE_STATUS = -1;
    public static int GREEN_STATUS = 0;
    public static int RED_STATUS = 1;

    private Position frameStart; // if not null, frame from start to current position is drawn

    public EntitySelector() {
        position = new Position();
    }

    @Override
    public void init() {
        localMap = GameMvc.instance().model().get(LocalMap.class);
        selectorSprite = new TextureRegion(new Texture("sprites/ui_tiles.png"), 0, 406, 64, 96);
        position.set(localMap.xSize / 2, localMap.ySize / 2, localMap.zSize - 1);
        cachePosition = new Position();
    }

    /**
     * Sets position of selector to the ground surface in the center of the map.
     */
    public void setToMapCenter() {
        int z = localMap.zSize - 1;
        while (z > 0 && BlockTypesEnum.SPACE.CODE == localMap.getBlockType(position.x, position.y, z)) {
            z--;
        }
        position.z = z;
        GameMvc.instance().view().localWorldStage.getCamera().centerCameraToPosition(position);
    }

    public void moveSelector(int dx, int dy, int dz) {
        cachePosition.set(position);
        position.add(dx, dy, dz);
        localMap.normalizePosition(position);
        if (!cachePosition.equals(position)) selectorMoved();
    }

    private void selectorMoved() {
        updateStatusAndSprite();
        GameMvc.instance().view().localWorldStage.getCamera().handleSelectorMove();
    }

    /**
     * Tries to update status sprite by position validator.
     */
    public void updateStatusAndSprite() {
        if (positionValidator != null) {
            int valid = positionValidator.validate(localMap, position) ? GREEN_STATUS : RED_STATUS;
            if (valid != status) { // status changed
                status = valid;
                statusSprite = getSpriteByStatus(status); //change sprite
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

    public int getStatus() {
        return status;
    }

    public TextureRegion getStatusSprite() {
        return statusSprite;
    }
}
