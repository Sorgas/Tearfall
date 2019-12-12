package stonering.enums.time;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum for units of in-game time that are constant for all worlds.
 *
 *
 * @author Alexander on 12.07.2019.
 */
public enum TimeUnitEnum {
    TICK("tick", 1),
    MINUTE("minute", 25),
    HOUR("hour", 60),
    DAY("day", 24),
    MONTH("month", 20),
    YEAR("year", 12);

    public final String NAME;
    public final int LENGTH; // in previous level utits

    private static Map<String, Integer> map;

    static {
        map = new HashMap<>();
        for (TimeUnitEnum unit : TimeUnitEnum.values()) {
            map.put(unit.NAME, unit.LENGTH);
        }
    }

    TimeUnitEnum(String name, int length) {
        NAME = name;
        LENGTH = length;
    }

    public TimeUnit getTimeUnit() {
        return new TimeUnit(this, map.get(NAME));
    }
}