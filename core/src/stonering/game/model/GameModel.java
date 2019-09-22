package stonering.game.model;

import com.badlogic.gdx.utils.Timer;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.model.system.GameCalendar;
import stonering.game.model.system.ModelComponent;
import stonering.util.global.Initable;
import stonering.util.global.LastInitable;
import stonering.util.global.Logger;

import java.io.Serializable;
import java.util.*;

/**
 * Generic gameModel. Can store single objects of each class implementing interface {@link Initable}.
 * Can init components.
 *
 * @author Alexander on 04.02.2019.
 */
public abstract class GameModel implements Initable, Serializable {
    private TreeMap<Class, ModelComponent> components;
    private List<IntervalTurnableContainer> intervalContainers;
    private List<Turnable> turnableComponents; // not all components are Turnable
    private Timer timer;                 //makes turns for entity containers and calendar
    private boolean paused;

    public GameModel() {
        components = new TreeMap<>((o1, o2) -> {
            if (o1.equals(o2)) return 0;
            if (o2.isAssignableFrom(LastInitable.class)) return 1;
            return o1.getName().compareTo(o2.getName());
        });
        turnableComponents = new ArrayList<>();
        intervalContainers = new ArrayList<>();
        put(new GameCalendar());
    }

    public <T extends ModelComponent> T get(Class<T> type) {
        return (T) components.get(type);
    }

    public <T extends ModelComponent> void put(T object) {
        components.put(object.getClass(), object);
        if (object instanceof IntervalTurnableContainer) { // map container to its interval
            intervalContainers.add((IntervalTurnableContainer) object);
        } else if (object instanceof Turnable) {
            turnableComponents.add((Turnable) object);
        }
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
                if (!paused) turnableComponents.forEach(Turnable::turn); // calendar turns others on time unit end
            }
        }, 0, 1f / 60);
    }

    /**
     * Called from {@link GameCalendar}
     */
    public void turn(TimeUnitEnum unit) {
        intervalContainers.forEach(container -> container.turnInterval(unit));
    }

    public void turn() {
        turnableComponents.forEach(Turnable::turn);
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
        if (paused) {
            timer.stop();
            Logger.GENERAL.logDebug("Game paused");
        } else {
            timer.start();
            Logger.GENERAL.logDebug("Game unpaused");
        }
    }
}
