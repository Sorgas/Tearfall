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
    private final HashMap<TimeUnitEnum, List<EntitySystem<T>>> entitySystems; // entities are updated with systems from this map
    private final HashMap<TimeUnitEnum, List<UtilitySystem>> utilitySystems;

    public EntityContainer() {
        entities = new ArrayList<>();
        entitySystems = new HashMap<>();
        utilitySystems = new HashMap<>();
        for (TimeUnitEnum value : TimeUnitEnum.values()) {
            entitySystems.put(value, new ArrayList<>());
            utilitySystems.put(value, new ArrayList<>());
        }
    }

    public void update(TimeUnitEnum unit) {
        entitySystems.get(unit).forEach(system -> entities.stream().filter(system.filteringPredicate).forEach(system::update));
        utilitySystems.get(unit).forEach(UtilitySystem::update);
    }

    public <S extends EntitySystem<T>> void putSystem(S system) {
        entitySystems.get(system.updateInterval).add(system);
    }

    public void putSystem(UtilitySystem system) {
        utilitySystems.get(system.updateInterval).add(system);
    }
}
