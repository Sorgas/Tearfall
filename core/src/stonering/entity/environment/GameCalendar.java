package stonering.entity.environment;

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
public class GameCalendar extends Turnable implements ModelComponent, Initable {
    public static int MINUTE_SIZE = 25;
    public static int HOUR_SIZE = 60;
    public static int DAY_SIZE = 24;
    public static int MONTH_SIZE = 20;
    public static int YEAR_SIZE = 12;
    public static final String MINUTE = "minute";
    public static final String HOUR = "hour";
    public static final String DAY = "day";
    public static final String MONTH = "month";
    public static final String YEAR = "year";

    private HashMap<String, List<IntervalTurnable>> listeners;
    private int time;
    private int minute;
    private int hour;
    private int day;
    private int month;
    private int year;

    public GameCalendar() {
        listeners = new HashMap<>();
        ArrayList<IntervalTurnable> mock = new ArrayList<>(Collections.singleton(new MockTurnable()));
        listeners.put(MINUTE, mock);
        listeners.put(HOUR, mock);
        listeners.put(DAY, mock);
        listeners.put(MONTH, mock);
        listeners.put(YEAR, mock);
    }

    @Override
    public void init() {

    }

    /**
     * For tick.
     */
    @Override
    public void turn() {
        time++;
        if (time >= MINUTE_SIZE) {
            time = 0;
            minute++;
            listeners.get(MINUTE).forEach(IntervalTurnable::turnMinute);
            if (minute >= HOUR_SIZE) {
                minute = 0;
                hour++;
                listeners.get(HOUR).forEach(IntervalTurnable::turnHour);
                if (hour >= DAY_SIZE) {
                    hour = 0;
                    day++;
                    listeners.get(DAY).forEach(IntervalTurnable::turnDay);
                    if (day >= MONTH_SIZE) {
                        day = 0;
                        month++;
                        listeners.get(MONTH).forEach(IntervalTurnable::turnMonth);
                        if (month >= YEAR_SIZE) {
                            month = 0;
                            year++;
                            listeners.get(YEAR).forEach(IntervalTurnable::turnYear);
                        }
                    }
                }
            }
        }
    }

    public void addListener(String key, IntervalTurnable listener) {
        if (listeners.containsKey(key)) {
            listeners.get(key).add(listener);
        } else {
            listeners.put(key, Arrays.asList(listener));
        }
    }

    private class MockTurnable extends IntervalTurnable {
    }

    public String getCurrentDate() {
        return year + ":" + month + ":" + day + ":" + hour + ":" + minute;
    }

    public int getTime() {
        return time;
    }

    public int getMinute() {
        return minute;
    }

    public int getHour() {
        return hour;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }
}
