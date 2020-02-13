package stonering.game.model.system;

import stonering.entity.Entity;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.model.Updatable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Container for {@link Entity}. Updates its entities via calling {@link EntitySystem}s.
 * Each system is mapped to its interval for update.
 *
 * @author Alexander on 17.07.2019.
 */
public abstract class EntityContainer<T extends Entity> implements ModelComponent, Updatable {
    public final List<T> entities;
    private final HashMap<TimeUnitEnum, List<EntitySystem<T>>> updateMapping; // entities are updated with systems from this map

    public EntityContainer() {
        entities = new ArrayList<>();
        updateMapping = new HashMap<>();
        for (TimeUnitEnum value : TimeUnitEnum.values()) {
            updateMapping.put(value, new ArrayList<>());
        }
    }

    public void update(TimeUnitEnum unit) {
        updateMapping.get(unit).forEach(system -> entities.stream().filter(system.filteringPredicate).forEach(system::update));

    }

    public <S extends EntitySystem<T>> void putSystem(S system) {
        updateMapping.get(system.updateInterval).add(system);
    }
}
