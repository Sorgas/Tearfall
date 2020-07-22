package stonering.enums.time;

/**
 * Enum for units of in-game time that are constant for all worlds.
 * Estimations are given for 60 fps.
 *
 * @author Alexander on 12.07.2019.
 */
public enum TimeUnitEnum {
    TICK("tick", 1),        // 1/60 real second
    MINUTE("minute", 25),   // 5/12 real second 
    HOUR("hour", 60),       // 1500 ticks, 25 real seconds
    DAY("day", 24),         // 10 real minutes
    MONTH("month", 20),     // TODO make month and seasons with different lengths and names.
    YEAR("year", 12);

    public final String NAME;
    public final int SIZE; // in previous level utils

    TimeUnitEnum(String name, int length) {
        NAME = name;
        SIZE = length;
    }
}
