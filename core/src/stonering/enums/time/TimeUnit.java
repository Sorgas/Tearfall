package stonering.enums.time;

/**
 * Represent unit of time, like hours and days.
 * Has size, counted in other units.
 *
 * @author Alexander on 15.07.2019.
 */
public class TimeUnit {
    public final TimeUnitEnum unit;
    private int size;
    public int state;

    public TimeUnit(TimeUnitEnum unit, int size) {
        this.unit = unit;
        this.size = size;
    }

    /**
     * Adds 1 to state. If state reaches size, resets it and returns true;
     */
    public boolean increment() {
        return (++state < size || (state = 0) == 0) && state == 0;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
