package stonering.game.model.system;

import com.badlogic.gdx.utils.Timer;

import stonering.entity.world.calendar.WorldCalendar;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.util.logging.Logger;

/**
 * Rolls time for game.
 * {@link Timer} provides update calls with specific interval ({@link GameTime#gameSpeed}).
 * Then, states {@link TimeUnitState}s updated(clock thing).
 * Largest updated time unit is passed to {@link GameModel} to update systems.
 * After day is over, {@link WorldCalendar} is notified to change game date.
 *
 * @author Alexander on 07.10.2018.
 */
public class GameTime {
    public final TimeUnitState tick;
    public final TimeUnitState minute; // ticks of minute
    public final TimeUnitState hour; // minutes of hour
    public final TimeUnitState day; // hours of day
    private TimeUnitState[] units; // for storing 'next' relation between units

    private Timer timer;                 //makes turns for entity containers and calendar
    public boolean paused;
    private int gameSpeed = 1;
    private final Timer.Task timerTask; // rolls time

    public GameTime() {
        tick = new TimeUnitState(TimeUnitEnum.TICK);
        minute = new TimeUnitState(TimeUnitEnum.MINUTE);
        hour = new TimeUnitState(TimeUnitEnum.HOUR);
        day = new TimeUnitState(TimeUnitEnum.DAY);
        units = new TimeUnitState[]{tick, minute, hour, day};
        timer = new Timer();
        timerTask = new Timer.Task() {
            @Override
            public void run() {
                if (!paused) turnUnit(1); // calendar turns other components
            }
        };
    }

    private void turnUnit(int index) {
        if (index >= units.length) return; // day ended in previous call, call world calendar
        if (units[index].increment()) { // unit ended
            turnUnit(index + 1); // increase next unit (on minute end, hour gets +1)
        } else {
            GameMvc.model().update(units[index - 1].unit); //update with previous unit
        }
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

    public void setGameSpeed(int speed) {
        gameSpeed = speed;
        timer.clear();
        initTimer();
    }

    public void initTimer() {
        timer.scheduleTask(timerTask, 0, 1f / 60 / gameSpeed);
    }

    public void singleUpdate() {
        if (paused) turnUnit(1);
    }

    public static class TimeUnitState {
        private final TimeUnitEnum unit;
        public int progress;
        public int max;

        TimeUnitState(TimeUnitEnum unit) {
            this.unit = unit;
            max = unit.SIZE;
        }
        
        boolean increment() {
            return ++progress >= unit.SIZE && (progress = 0) == 0;
        }
    }
}
