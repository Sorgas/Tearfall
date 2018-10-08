package stonering.objects.local_actors.environment;

import stonering.game.core.model.Turnable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Date and time storing class.
 *
 * @author Alexander on 07.10.2018.
 */
public class Calendar implements Turnable {
    private HashMap<String, List<CalendarListener>> listeners;
    private int minuteSize = 60;
    private int hourSize = 60;
    private int daySize = 24;
    private int monthSize = 30;
    private int yearSize = 12;

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
        if (time >= minuteSize) {
            time = 0;
            minute++;
            callListeners("minute");
            if (minute >= hourSize) {
                minute = 0;
                hour++;
                callListeners("hour");
                if (hour >= daySize) {
                    hour = 0;
                    day++;
                    callListeners("day");
                    if (day >= monthSize) {
                        day = 0;
                        month++;
                        callListeners("month");
                        if (month >= yearSize) {
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
