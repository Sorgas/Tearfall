package stonering.enums.time;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum for units of in-game time and their sizes in ticks.
 *
 * @author Alexander on 12.07.2019.
 */
public enum TimeUnitEnum {
    MINUTE("minute", 25),
    HOUR("hour", 60),
    DAY("day", 24),
    MONTH("month", 20),
    YEAR("year", 12);

    public final String NAME;
    public final int LENGTH;

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
        return new TimeUnit(NAME, map.get(NAME));
    }
}