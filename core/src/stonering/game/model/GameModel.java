package stonering.game.model;

import com.badlogic.gdx.utils.Timer;
import stonering.util.global.Initable;
import stonering.util.global.LastInitable;
import stonering.util.global.TagLoggersEnum;

import java.io.Serializable;
import java.util.TreeMap;

/**
 * Generic gameModel. Can store single objects of each class implementing interface {@link Initable}.
 * Can init components.
 *
 * @author Alexander on 04.02.2019.
 */
public abstract class GameModel implements Initable, Serializable {
    private TreeMap<Class, ModelComponent> components;
    private Timer timer;                 //makes turns for entity containers and calendar.
    private boolean paused;

    public GameModel() {
        components = new TreeMap<>((o1, o2) -> {
            if (o1.equals(o2)) return 0;
            if (o2.isAssignableFrom(LastInitable.class)) return 1;
            return o1.getName().compareTo(o2.getName());
        });
    }

    public <T extends ModelComponent> T get(Class<T> type) {
        return (T) components.get(type);
    }

    public <T extends ModelComponent> void put(T object) {
        components.put(object.getClass(), object);
    }

    /**
     * Inits all stored components that are {@link Initable}.
     * Used for components binding.
     */
    @Override
    public void init() {
        components.values().forEach(component -> {
            if (component instanceof Initable) ((Initable) component).init();
        });
        timer = new Timer();
        paused = true;
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                turn();
            }
        }, 0, 1f / 60);
    }

    /**
     * Turns all {@link Turnable components}
     */
    protected void turn() {
        if (paused) return;
        components.keySet().forEach(aClass -> {
            if (Turnable.class.isAssignableFrom(aClass)) {
                ((Turnable) components.get(aClass)).turn();
            }
        });
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        TagLoggersEnum.GENERAL.logDebug("Game paused set to " + paused);
        if (paused) {
            timer.stop();
            this.paused = true;
        } else {
            timer.start();
            this.paused = false;
        }
    }
}
