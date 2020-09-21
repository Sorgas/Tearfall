package stonering.game.model.system;

import stonering.entity.Entity;
import stonering.enums.time.TimeUnitEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

/**
 * Container for {@link Entity}. Updates its entities via calling {@link EntitySystem}s.
 * Each system is mapped to its interval for update.
 *
 * @author Alexander on 17.07.2019.
 */
public abstract class EntityContainer<T extends Entity> extends AbstractContainer<T> {
    private final HashMap<TimeUnitEnum, List<EntitySystem<T>>> entitySystems; // entities are updated with systems from this map

    public EntityContainer() {
        super();
        entitySystems = new HashMap<>();
        Arrays.stream(TimeUnitEnum.values()).forEach(value -> entitySystems.put(value, new ArrayList<>()));
    }

    @Override
    public void update(TimeUnitEnum unit) {
        super.update(unit);
        entitySystems.get(unit).forEach(system -> objects.stream().filter(system.filteringPredicate).forEach(system::update));
    }

    @Override
    public <S extends System> void addSystem(S system) {
        super.addSystem(system);
        if(system instanceof EntitySystem) entitySystems.get(system.updateInterval).add((EntitySystem) system);
    }

    public Stream<T> stream() {
        return objects.stream();
    }
}
