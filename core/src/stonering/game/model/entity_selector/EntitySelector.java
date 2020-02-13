package stonering.game.model.entity_selector;

import stonering.entity.Aspect;
import stonering.entity.Entity;
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

    public EntitySelector(Position position) {
        super(position);
    }
}