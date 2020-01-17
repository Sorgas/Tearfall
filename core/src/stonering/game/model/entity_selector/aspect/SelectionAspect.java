package stonering.game.model.entity_selector.aspect;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.util.geometry.Position;
import stonering.util.global.Executor;
import stonering.util.validation.PositionValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Part of {@link EntitySelector} that contain login for handling selection, cancellation.
 * Handler consumers will obtain positions to be applied to.
 *
 * @author Alexander on 10.01.2020
 */
public class SelectionAspect extends Aspect {
    public Runnable selectPreHandler; // executed once
    public Consumer<Position> selectHandler; // called for each position in selection box
    public Consumer<Position> defaultSelectHandler; // for selecting entities
    public Consumer<Position> cancelHandler;
    public Runnable postHandler; // clears scope map

    public PositionValidator defaultValidator;
    public PositionValidator validator;
    public Consumer<Boolean> validationConsumer; // used to update render

    public final Map<String, Object> selectionScope; // can hold some data between handler calls

    public SelectionAspect(Entity entity) {
        super(entity);
        selectionScope = new HashMap<>();
        defaultSelectHandler = (position) -> GameMvc.instance().view().showEntityStage(position);
        defaultValidator = position -> true;
        validationConsumer = status -> {};
        postHandler = selectionScope::clear;
    }

    public PositionValidator getValidator() {
        return validator == null ? defaultValidator : validator;
    }

    public Consumer<Position> getHandler() {
        return selectHandler == null ? defaultSelectHandler : selectHandler;
    }
}
