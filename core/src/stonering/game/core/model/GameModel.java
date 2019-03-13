package stonering.game.core.model;

import com.badlogic.gdx.utils.Timer;
import stonering.util.global.Initable;
import stonering.util.global.TagLoggersEnum;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Generic gameModel. Can store single objects of each class implementing interface {@link Initable}.
 * Can init components.
 *
 * @author Alexander on 04.02.2019.
 */
public abstract class GameModel implements Initable, Serializable {
    private HashMap<Class, ModelComponent> components;
    private Timer timer;                 //makes turns for entity containers and calendar.
    private boolean paused;

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
        timer = new Timer();
        paused = false;
        startContainer();
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

    private void startContainer() {
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                turn();
            }
        }, 0, 1f / 60);
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
