package stonering.entity.local.environment;

import stonering.game.core.model.IntervalTurnable;
import stonering.game.core.model.Turnable;

import java.util.*;

/**
 * Date and time storing class. Makes turns to roll time.
 *
 * @author Alexander on 07.10.2018.
 */
public class GameCalendar extends Turnable {
    private HashMap<String, List<IntervalTurnable>> listeners;
    private static int MINUTE_SIZE = 60;
    private static int HOUR_SIZE = 60;
    private static int DAY_SIZE = 24;
    private static int MONTH_SIZE = 30;
    private static int YEAR_SIZE = 12;

    private int time;
    private int minute;
    private int hour;
    private int day;
    private int month;
    private int year;

    public GameCalendar() {
        listeners = new HashMap<>();
        ArrayList<IntervalTurnable> mock = new ArrayList<>(Collections.singleton(new MockTurnable()));
        listeners.put("minute", mock);
        listeners.put("hour", mock);
        listeners.put("day", mock);
        listeners.put("month", mock);
        listeners.put("year", mock);
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
            listeners.get("minute").forEach(IntervalTurnable::turnMinute);
            if (minute >= HOUR_SIZE) {
                minute = 0;
                hour++;
                listeners.get("hour").forEach(IntervalTurnable::turnHour);
                if (hour >= DAY_SIZE) {
                    hour = 0;
                    day++;
                    listeners.get("day").forEach(IntervalTurnable::turnDay);
                    if (day >= MONTH_SIZE) {
                        day = 0;
                        month++;
                        listeners.get("month").forEach(IntervalTurnable::turnMonth);
                        if (month >= YEAR_SIZE) {
                            year++;
                            listeners.get("year").forEach(IntervalTurnable::turnYear);
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

    private class MockTurnable extends IntervalTurnable {}

    public void init() {
        //TODO
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
