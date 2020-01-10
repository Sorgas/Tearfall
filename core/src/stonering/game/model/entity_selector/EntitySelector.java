package stonering.game.model.entity_selector;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

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
public class EntitySelector extends Entity {
    private TextureRegion selectorSprite; // shows selector position, and selected designation.
    private TextureRegion statusSprite;   // indicates position validity.
    private Position frameStart; // if not null, frame from start to current position is drawn

    public EntitySelector(Position position) {
        super(position);
    }

    @Override
    public void init() {
        LocalMap localMap = GameMvc.instance().model().get(LocalMap.class);
        selectorSprite = new TextureRegion(new Texture("sprites/ui_tiles.png"), 0, 406, 64, 96);
        position.set(localMap.xSize / 2, localMap.ySize / 2, localMap.zSize - 1);
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
