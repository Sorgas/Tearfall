package stonering.game.model.system;

import stonering.enums.time.TimeUnitEnum;
import stonering.util.lang.Updatable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Model component which contains some objects and can update them with {@link System}.
 *
 * @author Alexander on 16.03.2020.
 */
public abstract class AbstractContainer<T> implements ModelComponent, Updatable {
    public final List<T> objects = new ArrayList<>();
    public final List<T> toRemove = new ArrayList<>(); // TODO add removing of entity
    private final HashMap<TimeUnitEnum, List<UtilitySystem>> systems = new HashMap<>();

    public AbstractContainer() {
        Arrays.stream(TimeUnitEnum.values()).forEach(value -> systems.put(value, new ArrayList<>()));
    }

    public void update(TimeUnitEnum unit) {
        toRemove.forEach(objects::remove);
        systems.get(unit).forEach(UtilitySystem::update);
    }
    
    public void add(T object) {
        objects.add(object);
    }

    public void remove(T object) {
        if(objects.contains(object)) toRemove.add(object);
    }

    public <S extends System> void addSystem(S system) {
        if(system instanceof UtilitySystem) systems.get(system.updateInterval).add((UtilitySystem) system);
    }
}
