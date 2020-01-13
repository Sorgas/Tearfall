package stonering.game.model.entity_selector.aspect;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.entity.unit.aspects.RenderAspect;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.util.validation.PositionValidator;

import java.util.function.Consumer;

/**
 * Validates single position of {@link EntitySelector}.
 * After validation can update {@link RenderAspect} of selector.
 *
 * @author Alexander on 10.01.2020
 */
public class ValidationAspect extends Aspect {
    public PositionValidator validator;
    public Consumer<Boolean> validationConsumer;

    public ValidationAspect(Entity entity) {
        super(entity);
        validationConsumer = (status) -> {};
    }
}
