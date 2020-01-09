package stonering.game.model.entity_selector;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.util.global.Initable;

/**
 * Players 'mouse' in the game. Selects objects on local map. Moved by mouse or WASDRF. When is moved by mouse sprite is not shown.
 * Selector can have tool selected (like designating digging) for creating in-game orders (tools implemented with {@link Aspect}).
 * Selector can have additional sprite to indicate whether position suits for building or not.
 * If positionValidator exists, position will be validated on move, updating sprite.
 * <p>
 * //TODO divide entity selecting and rendering (change this to selector, add camera class).
 * //TODO add binds for entities and positions (numbers prob.).
 *
 * @author Alexander Kuzyakov on 10.12.2017.
 */
public class EntitySelector extends Entity implements Initable {
    private LocalMap localMap;
    private TextureRegion selectorSprite; // shows selector position, and selected designation.
    private TextureRegion statusSprite;   // indicates position validity.
    private Position frameStart; // if not null, frame from start to current position is drawn

    public EntitySelector() {
        position = new Position();
    }

    @Override
    public void init() {
        localMap = GameMvc.instance().model().get(LocalMap.class);
        selectorSprite = new TextureRegion(new Texture("sprites/ui_tiles.png"), 0, 406, 64, 96);
        position.set(localMap.xSize / 2, localMap.ySize / 2, localMap.zSize - 1);

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

    public Position getFrameStart() {
        return frameStart;
    }

    public void setFrameStart(Position frameStart) {
        this.frameStart = frameStart;
    }

    public TextureRegion getSelectorSprite() {
        return selectorSprite;
    }

    public TextureRegion getStatusSprite() {
        return statusSprite;
    }
}
