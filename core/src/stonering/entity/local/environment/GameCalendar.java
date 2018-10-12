package stonering.entity.local.environment;

import stonering.game.core.model.Turnable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Date and time storing class. Makes turns to roll time.
 *
 * @author Alexander on 07.10.2018.
 */
public class GameCalendar extends Turnable {
    private HashMap<String, List<CalendarListener>> listeners;
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

    /**
     * For tick.
     */
    @Override
    public void turn() {
        time++;
        if (time >= MINUTE_SIZE) {
            time = 0;
            minute++;
            callListeners("minute");
            if (minute >= HOUR_SIZE) {
                minute = 0;
                hour++;
                callListeners("hour");
                if (hour >= DAY_SIZE) {
                    hour = 0;
                    day++;
                    callListeners("day");
                    if (day >= MONTH_SIZE) {
                        day = 0;
                        month++;
                        callListeners("month");
                        if (month >= YEAR_SIZE) {
                            year++;
                            callListeners("year");
                        }
                    }
                }
            }
        }
    }

    private void callListeners(String key) {
        if (listeners.containsKey(key)) {
            listeners.get(key).forEach(CalendarListener::invoke);
        }
    }

    public void addListener(String key, CalendarListener listener) {
        if (listeners.containsKey(key)) {
            listeners.get(key).add(listener);
        } else {
            listeners.put(key, Arrays.asList(listener));
        }
    }

    public interface CalendarListener {
        void invoke();
    }

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
