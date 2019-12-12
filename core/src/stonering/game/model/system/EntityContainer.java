package stonering.game.model.system;

import stonering.entity.Entity;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.model.Turnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Container for {@link Entity}. Updates its entities via calling {@link EntitySystem}s.
 * Each system is mapped to its interval for update.
 *
 * @author Alexander on 17.07.2019.
 */
public abstract class EntityContainer<T extends Entity> implements ModelComponent, Turnable {
    public final List<T> entities;
    private final HashMap<Class, EntitySystem<T>> systems;
    private final HashMap<TimeUnitEnum, List<EntitySystem<T>>> updateMapping;

    public EntityContainer() {
        entities = new ArrayList<>();
        systems = new HashMap<>();
        updateMapping = new HashMap<>();
    }

    public void turn(TimeUnitEnum unit) {
        updateMapping.get(unit).forEach(system -> entities.forEach(system::update));
    }

    public <S extends EntitySystem<T>> S getSystem(Class<S> type) {
        return (S) systems.get(type);
    }

    public <S extends EntitySystem<T>> void putSystem(S system) {
        systems.put(system.getClass(), system);
        updateMapping.getOrDefault(system.updateInterval, new ArrayList<>()).add(system);
    }
}
