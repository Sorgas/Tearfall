package stonering.game.model;

import com.badlogic.gdx.utils.Timer;
import stonering.game.model.system.GameCalendar;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.model.system.ModelComponent;
import stonering.util.global.Initable;
import stonering.util.global.LastInitable;
import stonering.util.global.Logger;

import java.io.Serializable;
import java.util.TreeMap;

/**
 * Generic gameModel. Can store single objects of each class implementing interface {@link Initable}.
 * Can init components.
 *
 * @author Alexander on 04.02.2019.
 */
public abstract class GameModel extends IntervalTurnable implements Initable, Serializable {
    private TreeMap<Class, ModelComponent> components;
    private Timer timer;                 //makes turns for entity containers and calendar.
    private GameCalendar calendar;
    private boolean paused;

    public GameModel() {
        components = new TreeMap<>((o1, o2) -> {
            if (o1.equals(o2)) return 0;
            if (o2.isAssignableFrom(LastInitable.class)) return 1;
            return o1.getName().compareTo(o2.getName());
        });
        put(calendar = new GameCalendar());
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
            if (component instanceof Initable) {
                Logger.LOADING.logDebug("Initing model component: " + component.getClass().getSimpleName());
                ((Initable) component).init();
            }
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
     * Turns all {@link Turnable components}. This is an entry point from timer.
     * GameCalendar is turned from here, and then turns model for intervals.
     */
    public void turn() {
        if (paused) return;
        components.keySet().forEach(aClass -> {
            if (Turnable.class.isAssignableFrom(aClass)) {
                ((Turnable) components.get(aClass)).turn();
            }
        });
    }

    /**
     * Called by {@link GameCalendar}.
     */
    @Override
    public void turnInterval(TimeUnitEnum unit) {
        components.keySet().forEach(aClass -> {
            if (IntervalTurnable.class.isAssignableFrom(aClass)) {
                ((IntervalTurnable) components.get(aClass)).turnInterval(unit);
            }
        });
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        Logger.GENERAL.logDebug("Game paused set to " + paused);
        if (paused) {
            timer.stop();
            this.paused = true;
        } else {
            timer.start();
            this.paused = false;
        }
    }
}
