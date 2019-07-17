package stonering.entity.environment;

import stonering.enums.time.TimeUnit;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.GameMvc;
import stonering.game.model.IntervalTurnable;
import stonering.game.model.Turnable;
import stonering.game.model.lists.ModelComponent;

/**
 * Date and time storing class. Makes turns to roll time.
 * On creation, sizes of units are taken from {@link TimeUnitEnum}, then they can be altered.
 *
 * @author Alexander on 07.10.2018.
 */
public class GameCalendar extends Turnable implements ModelComponent {
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
        if (index >= units.length || !units[index].increment()) return; // if out of array, or unit not ended
        GameMvc.instance().getModel().turnInterval(units[index].unit);
        turnUnit(index + 1);
    }

    public String getCurrentDate() {
        return year.state + ":" + month.state + ":" + day.state + ":" + hour.state + ":" + minute.state;
    }
}
