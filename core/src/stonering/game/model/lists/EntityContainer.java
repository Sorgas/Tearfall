package stonering.game.model.lists;

import stonering.entity.Entity;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.model.IntervalTurnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Container for {@link Entity}. Turns its entities.
 *
 * @author Alexander on 17.07.2019.
 */
public abstract class EntityContainer<T extends Entity> extends IntervalTurnable implements ModelComponent {
    protected List<T> entities;

    public EntityContainer() {
        entities = new ArrayList<>();
    }

    @Override
    public void turnInterval(TimeUnitEnum unit) {
        entities.forEach(entity -> entity.turnInterval(unit));
    }

    @Override
    public void turn() {
        entities.forEach(T::turn);
    }
}
