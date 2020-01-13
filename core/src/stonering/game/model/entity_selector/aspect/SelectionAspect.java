package stonering.game.model.entity_selector.aspect;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.util.geometry.Position;
import stonering.util.global.Executor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Part of {@link EntitySelector} that handles selection and cancellation input.
 * Handler consumers will obtain positions to be applied to.
 *
 * @author Alexander on 10.01.2020
 */
public class SelectionAspect extends Aspect {
    public Consumer<Position> selectHandler; // called for each position in selection box
    public Consumer<Position> defaultSelectHandler; // for selecting entities
    public Consumer<Position> cancelHandler;
    public Executor selectPreHandler; // executed once
    public final Map<String, Object> selectionScope;

    public SelectionAspect(Entity entity) {
        super(entity);
        this.defaultSelectHandler = (position) -> GameMvc.instance().view().showEntityStage(position);
        selectionScope = new HashMap<>();
    }
}
