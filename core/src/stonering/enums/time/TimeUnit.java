package stonering.enums.time;

/**
 * Represent progress of time unit, like hours and days.
 * Has size, counted in other units.
 *
 * @author Alexander on 15.07.2019.
 */
public class TimeUnit {
    public final TimeUnitEnum unit;
    public int max;
    public int progress;

    public TimeUnit(TimeUnitEnum unit) {
        this.unit = unit;
        max = unit.LENGTH;
    }

    /**
     * Adds 1 to state. If state reaches size, resets it and returns true;
     */
    public boolean increment() {
        return (++progress < max || (progress = 0) == 0) && progress == 0;
    }
}
