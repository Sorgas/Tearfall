package stonering.game.model.system;

import stonering.entity.Entity;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.model.Turnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Container for {@link Entity}. Turns its entities.
 *
 * @author Alexander on 17.07.2019.
 */
public abstract class EntityContainer<T extends Entity> implements ModelComponent, Turnable {
    protected List<T> entities;

    public EntityContainer() {
        entities = new ArrayList<>();
    }

    @Override
    public void turn() {
        entities.forEach(T::turn);
    }

    @Override
    public void turnUnit(TimeUnitEnum unit) {
        entities.forEach(entity -> entity.turnUnit(unit));
    }
}
