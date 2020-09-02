package stonering.game.model;

import java.io.Serializable;
import java.util.*;

import stonering.enums.time.TimeUnitEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.GameTime;
import stonering.game.model.system.ModelComponent;
import stonering.util.lang.Initable;
import stonering.util.lang.Updatable;

/**
 * Generic gameModel. Can store single objects of each class implementing interface {@link Initable}.
 * Can init components.
 *
 * @author Alexander on 04.02.2019.
 */
public abstract class GameModel implements Initable, Serializable, Updatable {
    private Map<Class, ModelComponent> components;
    private List<Updatable> updatableComponents; // not all components are Updatable
    public final GameTime gameTime;

    public GameModel() {
        components = new HashMap<>();
        updatableComponents = new ArrayList<>();
        gameTime = new GameTime();
    }

    public <T extends ModelComponent> T get(Class<T> type) {
        return (T) components.get(type);
    }

    public <T extends ModelComponent> Optional<T> optional(Class<T> type) {
        return Optional.ofNullable(get(type));
    }

    public <T extends ModelComponent> void put(T object) {
        components.put(object.getClass(), object);
        if (object instanceof Updatable) updatableComponents.add((Updatable) object);
    }

    /**
     * Inits all stored components that are {@link Initable}.
     * Used for components binding.
     */
    @Override
    public void init() {
        components.values().stream()
                .filter(component -> component instanceof Initable)
                .map(component -> (Initable) component)
                .forEach(Initable::init);
        gameTime.initTimer();
    }

    @Override
    public void update(TimeUnitEnum unit) {
        updatableComponents.forEach(component -> component.update(unit));
        if(unit == TimeUnitEnum.TICK) GameMvc.view().overlayStage.update(); // count model updates
    }
}
