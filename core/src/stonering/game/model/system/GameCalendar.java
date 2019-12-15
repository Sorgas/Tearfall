package stonering.game.model.system;

import stonering.entity.world.calendar.WorldCalendar;
import stonering.enums.time.TimeUnit;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.GameMvc;

/**
 * Date and time storing class. Makes turns to roll time.
 * Game model is updated by this component to pass correct time unit.
 * After day is over, {@link WorldCalendar} is notified to change game date.
 *
 * @author Alexander on 07.10.2018.
 */
public class GameCalendar {
    private final TimeUnit tick;
    public final TimeUnit minute;
    public final TimeUnit hour;
    public final TimeUnit day;
    private TimeUnit[] units; // for storing 'next' relation between units

    public GameCalendar() {
        tick = new TimeUnit(TimeUnitEnum.TICK);
        minute = new TimeUnit(TimeUnitEnum.MINUTE);
        hour = new TimeUnit(TimeUnitEnum.HOUR);
        day = new TimeUnit(TimeUnitEnum.DAY);
        units = new TimeUnit[]{tick, minute, hour, day};
    }

    public void turn() {
        turnUnit(1); // turnUnit minute
    }

    private void turnUnit(int index) {
        if (index >= units.length) { // day ended in previous call
            //yearNumber++; // call world calendar
            return;
        }
        if (units[index].increment()) { // unit ended
            turnUnit(index + 1); // increase next unit (on minute end, hour gets +1)
        } else {
            GameMvc.instance().model().update(units[index - 1].unit); //update with previous unit
        }
    }

    public String getCurrentDate() {
        return day.progress + ":" + hour.progress;
    }
}
