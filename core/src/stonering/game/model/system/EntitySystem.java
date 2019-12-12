package stonering.game.model.system;

import stonering.entity.Entity;
import stonering.enums.time.TimeUnitEnum;

/**
 * Superclass for all systems that work with entities.
 * Is updated by the by specified time interval.
 * TODO add 'target' aspects
 *
 * @author Alexander on 01.11.2019.
 */
public abstract class EntitySystem<T extends Entity> {
    public TimeUnitEnum updateInterval = TimeUnitEnum.TICK;

    public abstract void update(T entity);
}
