package stonering.game.model;

import stonering.entity.Entity;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.model.system.EntityContainer;
import stonering.game.model.system.ModelComponent;

/**
 * {@link EntityContainer} that can be turned once in a time unit. Used for {@link ModelComponent}s.
 *
 * @author Alexander on 23.09.2019.
 */
public class IntervalTurnableContainer<T extends Entity> extends EntityContainer<T> {

    public void turnInterval(TimeUnitEnum unit) {
    }
}
