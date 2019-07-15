package stonering.enums.time;

import stonering.game.model.IntervalTurnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent unit of time, like hours and days.
 * Has size, counted in other units.
 *
 * @author Alexander on 15.07.2019.
 */
public class TimeUnit {
    private String name;
    private int size;
    public int state;
    public List<IntervalTurnable> listeners;

    public TimeUnit(String name, int size) {
        this.name = name;
        this.size = size;
        listeners = new ArrayList<>();
    }

    /**
     * Adds 1 to state. If state reaches size, resets it and returns true;
     */
    public boolean increment() {
        return (++state < size || (state = 0) == 0) && state == 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
