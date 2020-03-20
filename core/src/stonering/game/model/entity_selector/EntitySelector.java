package stonering.game.model.entity_selector;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.util.geometry.IntVector2;
import stonering.util.geometry.Position;

/**
 * Players 'mouse' in the game. Selects objects on local map. Moved by mouse or WASDRF.
 * Has size that can be larger than 1 tile. Position points to left lower corner of selector.
 * When is moved by mouse sprite is not shown.
 * Selector can have tool selected (like designating digging) for creating in-game orders (tools implemented with {@link Aspect}).
 * Selector can have additional sprite to indicate whether position suits for building or not.
 *
 * @author Alexander Kuzyakov on 10.12.2017.
 */
public class EntitySelector extends Entity {
    public final IntVector2 size;

    public EntitySelector(Position position) {
        super(position);
        size = new IntVector2(1, 1);
    }
    
    public Position getOppositePosition() {
        return Position.add(position, size.x - 1, size.y - 1, 0);
    }
}
