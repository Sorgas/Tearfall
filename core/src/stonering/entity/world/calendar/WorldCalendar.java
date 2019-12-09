package stonering.entity.world.calendar;

/**
 * Four seasons are always present, but their names and lengths are different.
 * Season always consists of three months.
 * Names of 'months' are stored here. Months lengths can differ from each other.
 *
 * @author Alexander on 09.12.2019.
 */
public class WorldCalendar {
    public final Month[] months;
    public final String[] seasons; // spring, summer, autumn, winter

    public WorldCalendar() {
        months = new Month[12];
        seasons = new String[4];
    }
}
