package stonering.entity.environment;

import stonering.enums.time.TimeUnit;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.model.IntervalTurnable;
import stonering.game.model.lists.ModelComponent;
import stonering.game.model.Turnable;
import stonering.util.global.Initable;

import java.util.*;

/**
 * Date and time storing class. Makes turns to roll time.
 *
 * @author Alexander on 07.10.2018.
 */
public class GameCalendar extends Turnable implements ModelComponent {
    private Map<TimeUnitEnum, TimeUnit> units;

    public GameCalendar() {
        loadUnitsFromEnum();
    }

    @Override
    public void turn() {
        turnUnit(0);
    }

    private void turnUnit(int index) {
        if(index >= units.size()) return;
        units.get(index).listeners.forEach(IntervalTurnable::turnDay);
        if(units.get(index).increment()) turnUnit(index + 1);
    }

    public void addListener(String key, IntervalTurnable listener) {
        if (listeners.containsKey(key)) {
            listeners.get(key).add(listener);
        } else {
            listeners.put(key, Arrays.asList(listener));
        }
    }

    /**
     * Loads units from enum to list.
     * they than can be altered.
     */
    private void loadUnitsFromEnum() {
        units = new HashMap<>();
        for (TimeUnitEnum value : TimeUnitEnum.values()) {
            units.put(value, value.getTimeUnit());
        }
    }

    public String getCurrentDate() {
        return year + ":" + monthOfYear + ":" + dayOfMonth + ":" + hourOfDay + ":" + minuteOfHour;
    }

    public int getTicksOfMinute() {
        return ticksOfMinute;
    }

    public int getMinuteOfHour() {
        return minuteOfHour;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public int getMonthOfYear() {
        return monthOfYear;
    }

    public int getYear() {
        return year;
    }
}
