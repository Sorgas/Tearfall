package stonering.game.model.system;

import stonering.enums.time.TimeUnitEnum;
import stonering.game.model.Updatable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Model component which contains some objects and can update them with {@link System}.
 * @author Alexander on 16.03.2020.
 */
public abstract class AbstractContainer<T> implements ModelComponent, Updatable {
    public final List<T> objects;
    private final HashMap<TimeUnitEnum, List<UtilitySystem>> systems;

    public AbstractContainer() {
        objects = new ArrayList<>();
        systems = new HashMap<>();
        Arrays.stream(TimeUnitEnum.values()).forEach(value -> systems.put(value, new ArrayList<>()));
    }

    public void update(TimeUnitEnum unit) {
        systems.get(unit).forEach(UtilitySystem::update);
    }
    
    public <S extends System> void put(S system) {
        if(system instanceof UtilitySystem) systems.get(system.updateInterval).add((UtilitySystem) system);
    }
}
