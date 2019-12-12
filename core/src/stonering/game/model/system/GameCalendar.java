package stonering.game.model.system;

import stonering.enums.time.TimeUnit;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.GameMvc;
import stonering.game.model.Updatable;

/**
 * Date and time storing class. Makes turns to roll time.
 * All {@link ModelComponent}s are turned from here.
 * On creation, sizes of units are taken from {@link TimeUnitEnum}, then they can be altered.
 *
 * @author Alexander on 07.10.2018.
 */
public class GameCalendar {
    private int yearNumber;
    public final TimeUnit minute; // for direct access while rendering
    public final TimeUnit hour;
    public final TimeUnit day;
    public final TimeUnit month;
    public final TimeUnit year;
    private TimeUnit[] units; // for storing 'next' relation between units

    public GameCalendar() {
        minute = TimeUnitEnum.MINUTE.getTimeUnit();
        hour = TimeUnitEnum.HOUR.getTimeUnit();
        day = TimeUnitEnum.DAY.getTimeUnit();
        month = TimeUnitEnum.MONTH.getTimeUnit();
        year = TimeUnitEnum.YEAR.getTimeUnit();
        units = new TimeUnit[]{minute, hour, day, month, year};
    }

    public void turn() {
        turnUnit(0); // turnUnit minute
    }

    private void turnUnit(int index) {
        if (index >= units.length) { // year ended in previous call
            yearNumber++;
            return;
        }
        if (units[index].increment()) { // unit ended
            GameMvc.instance().model().getUpdatableComponents().forEach(component -> component.turnUnit(units[index].unit));
            turnUnit(index + 1); // increase next unit (on minute end, hour gets +1)
        } else if (index == 0) GameMvc.instance().model().getUpdatableComponents().forEach(Updatable::update); // regular turnUnit
    }

    public String getCurrentDate() {
        return yearNumber + ":" + year.state + ":" + month.state + ":" + day.state + ":" + hour.state;
    }
}
