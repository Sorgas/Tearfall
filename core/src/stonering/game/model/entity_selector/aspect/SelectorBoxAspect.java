package stonering.game.model.entity_selector.aspect;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.util.geometry.Position;
import stonering.util.global.Executor;
import stonering.util.validation.PositionValidator;

import java.util.function.Consumer;

/**
 * Aspect that holds selection box set by player.
 * Box last position is always {@link EntitySelector}'s position.
 * Has iterator that calls some {@link Executor} for selector position if it exists
 * and for all positions between selector's and box start if both exist.
 *
 * @author Alexander on 10.01.2020
 */
public class SelectorBoxAspect extends Aspect {
    public Position boxStart;
    public boolean enabled;
    public Consumer<Consumer<Position>> boxIterator;

    public SelectorBoxAspect(Entity entity) {
        super(entity);
        boxIterator = consumer -> {
            if (entity.position == null) return;
            Position internalBoxStart = boxStart != null ? boxStart : entity.position;
            PositionValidator validator = entity.getAspect(ValidationAspect.class).validator;
            for (int x = Math.min(internalBoxStart.x, entity.position.x); x <= Math.max(internalBoxStart.x, entity.position.x); x++) {
                for (int y = Math.min(internalBoxStart.y, entity.position.y); y <= Math.max(internalBoxStart.y, entity.position.y); y++) {
                    for (int z = Math.min(internalBoxStart.z, entity.position.z); z <= Math.max(internalBoxStart.z, entity.position.z); z++) {
                        Position position = new Position(x, y, z);
                        if(validator.validate(position)) consumer.accept(position);
                    }
                }
            }
        };
    }
}
