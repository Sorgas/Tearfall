package stonering.entity.world.calendar;

/**
 * @author Alexander on 09.12.2019.
 */
public class Month {
    public final String name;
    public final int length; // in days

    public Month(String name, int length) {
        this.name = name;
        this.length = length;
    }
}
