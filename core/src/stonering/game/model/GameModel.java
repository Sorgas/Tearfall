package stonering.game.model;

import com.badlogic.gdx.utils.Timer;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.GameTime;
import stonering.game.model.system.ModelComponent;
import stonering.util.global.Initable;
import stonering.util.logging.Logger;
import stonering.util.global.Updatable;

import java.io.Serializable;
import java.util.*;

/**
 * Generic gameModel. Can store single objects of each class implementing interface {@link Initable}.
 * Can init components.
 *
 * @author Alexander on 04.02.2019.
 */
public abstract class GameModel implements Initable, Serializable, Updatable {
    private Map<Class, ModelComponent> components;
    private List<Updatable> updatableComponents; // not all components are Updatable
    protected GameTime calendar;
    private Timer timer;                 //makes turns for entity containers and calendar
    public boolean paused;
    private int gameSpeed = 1;

    public GameModel() {
        components = new HashMap<>();
        updatableComponents = new ArrayList<>();
        calendar = new GameTime();
    }

    public <T extends ModelComponent> T get(Class<T> type) {
        return (T) components.get(type);
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
        components.values().forEach(component -> {
            if (component instanceof Initable) {
                Logger.LOADING.logDebug("Initing model component: " + component.getClass().getSimpleName());
                ((Initable) component).init();
            }
        });
        timer = new Timer();
        paused = true;
        initTimer();
    }

    @Override
    public void update(TimeUnitEnum unit) {
        updatableComponents.forEach(component -> component.update(unit));
        if(unit == TimeUnitEnum.TICK) GameMvc.view().overlayStage.update(); // count model updates
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

    public GameTime getCalendar() {
        return calendar;
    }

    public void setGameSpeed(int speed) {
        gameSpeed = speed;
        timer.clear();
        initTimer();
    }

    private void initTimer() {
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                if (!paused) calendar.update(); // calendar turns other components
            }
        }, 0, 1f / 60 / gameSpeed);
    }
    
    public void singleUpdate() {
        if(paused) calendar.update();
    }
}
