package stonering.game.model.entity_selector.aspect;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.util.global.Executor;

/**
 * Part of {@link EntitySelector} that handles selection and cancellation input.
 *
 * @author Alexander on 10.01.2020
 */
public class SelectionAspect extends Aspect {
    public Executor selectHandler;
    public Executor defaultSelectHandler;
    public Executor cancelHandler;

    public SelectionAspect(Entity entity) {
        super(entity);
        this.defaultSelectHandler = () -> GameMvc.instance().view().showEntityStage(entity.position);
    }
}
