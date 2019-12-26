package stonering.game.model.system;

import stonering.entity.world.calendar.WorldCalendar;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;

/**
 * Represents in-game time. This class is updated by timer, and then updates {@link GameModel}.
 * Current time unit is passed to model to notify appropriate systems.
 * After day is over, {@link WorldCalendar} is notified to change game date.
 *
 * @author Alexander on 07.10.2018.
 */
public class GameTime {
    private final TimeUnit tick;
    public final TimeUnit minute; // ticks of minute
    public final TimeUnit hour; // minutes of hour
    public final TimeUnit day; // hours of day
    private TimeUnit[] units; // for storing 'next' relation between units

    public GameTime() {
        tick = new TimeUnit(TimeUnitEnum.TICK);
        minute = new TimeUnit(TimeUnitEnum.MINUTE);
        hour = new TimeUnit(TimeUnitEnum.HOUR);
        day = new TimeUnit(TimeUnitEnum.DAY);
        units = new TimeUnit[]{tick, minute, hour, day};
    }

    public void update() {
        turnUnit(1); // turnUnit minute
    }

    private void turnUnit(int index) {
        if (index >= units.length) { // day ended in previous call
            return; // call world calendar
        }
        if (units[index].increment()) { // unit ended
            turnUnit(index + 1); // increase next unit (on minute end, hour gets +1)
        } else {
            GameMvc.instance().model().update(units[index - 1].unit); //update with previous unit
        }
    }

    public String getCurrentTime() {
        return day.progress + " : " + hour.progress;
    }

    public class TimeUnit {
        private final TimeUnitEnum unit;
        private int progress;
        public int max;

        TimeUnit(TimeUnitEnum unit) {
            this.unit = unit;
            max = unit.SIZE;
        }

        /**
         * Adds 1 to state. If state reaches size, resets it and returns true;
         */
        boolean increment() {
            return ++progress >= unit.SIZE && (progress = 0) == 0;
        }
    }
}
