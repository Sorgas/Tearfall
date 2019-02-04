package stonering.game.core.model;

import stonering.util.global.Initable;

import java.util.HashMap;

/**
 * Generic gameModel. Can store single objects of each class implementing interface {@link Initable}.
 * Can init components.
 *
 * @author Alexander on 04.02.2019.
 */
public abstract class GameModel implements Initable {
    private HashMap<Class, ModelComponent> components;

    public GameModel() {
        components = new HashMap<>();
    }

    public <T extends ModelComponent> T get(Class<T> type) {
        return (T) components.get(type);
    }

    public <T extends ModelComponent> void put(T object) {
        components.put(object.getClass(), object);
    }

    /**
     * Inits all stored components that are {@link Initable}.
     */
    @Override
    public void init() {
        components.keySet().forEach(aClass -> {
            if (Initable.class.isAssignableFrom(aClass)) {
                ((Initable) components.get(aClass)).init();

            }
        });
    }

    /**
     * Turns all {@link Turnable components}
     */
    protected void turn() {
        components.keySet().forEach(aClass -> {
            if (Turnable.class.isAssignableFrom(aClass)) {
                ((Turnable) components.get(aClass)).turn();
            }
        });
    }
}
