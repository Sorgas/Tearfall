package stonering.game.model;

import com.badlogic.gdx.utils.Timer;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.GameCalendar;
import stonering.game.model.system.ModelComponent;
import stonering.util.global.Initable;
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
    private Map<Class, ModelComponent> components;
    private List<Turnable> turnableComponents; // not all components are Turnable
    protected GameCalendar calendar;
    private Timer timer;                 //makes turns for entity containers and calendar
    private boolean paused;

    public GameModel() {
        components = new HashMap<>();
        turnableComponents = new ArrayList<>();
        calendar = new GameCalendar();
    }

    public <T extends ModelComponent> T get(Class<T> type) {
        return (T) components.get(type);
    }

    public <T extends ModelComponent> void put(T object) {
        components.put(object.getClass(), object);
        if (object instanceof Turnable) turnableComponents.add((Turnable) object);
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
                if (!paused) calendar.turn(); // calendar turns other components
            }
        }, 0, 1f / 60);
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

    public GameCalendar getCalendar() {
        return calendar;
    }

    public List<Turnable> getTurnableComponents() {
        return turnableComponents;
    }
}
