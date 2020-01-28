package stonering.game.model.system;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.enums.time.TimeUnitEnum;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Superclass for all systems that work with entities.
 * Is updated by specified time interval (every tick by default). Entities to update are filtered by target aspects.
 *
 * @author Alexander on 01.11.2019.
 */
public abstract class EntitySystem<T extends Entity> {
    public TimeUnitEnum updateInterval = TimeUnitEnum.TICK;
    public final Set<Class<? extends Aspect>> targetAspects = new HashSet<>();
    public final Predicate<Entity> filteringPredicate = (entity) -> entity.aspects.keySet().containsAll(targetAspects);

    public abstract void update(T entity);
}
