package stonering.enums.time;

/**
 * Enum for units of in-game time that are constant for all worlds.
 *
 *
 * @author Alexander on 12.07.2019.
 */
public enum TimeUnitEnum {
    TICK("tick", 1),
    MINUTE("minute", 25),
    HOUR("hour", 60), // 25 real seconds
    DAY("day", 24), // 10 real mins
    MONTH("month", 20), // TODO make month and seasons with different lengths and names.
    YEAR("year", 12);

    public final String NAME;
    public final int LENGTH; // in previous level utils

    TimeUnitEnum(String name, int length) {
        NAME = name;
        LENGTH = length;
    }
}
