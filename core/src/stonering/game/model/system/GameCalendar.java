package stonering.game.model.system;

import stonering.enums.time.TimeUnit;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.GameMvc;
import stonering.game.model.Turnable;

/**
 * Date and time storing class. Makes turns to roll time.
 * On creation, sizes of units are taken from {@link TimeUnitEnum}, then they can be altered.
 *
 * @author Alexander on 07.10.2018.
 */
public class GameCalendar extends Turnable implements ModelComponent {
    private int yearNumber; //
    public final TimeUnit minute; // for faster rendering
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

    @Override
    public void turn() {
        turnUnit(0);
    }

    private void turnUnit(int index) {
        if (index >= units.length) {
            yearNumber++;
            return;
        }
        if (!units[index].increment()) return; // or unit not ended
        GameMvc.instance().getModel().turnInterval(units[index].unit); // turn model on unit end
        turnUnit(index + 1); // increase next unit
    }

    public String getCurrentDate() {
        return yearNumber + ":" + year.state + ":" + month.state + ":" + day.state + ":" + hour.state;
    }
}
