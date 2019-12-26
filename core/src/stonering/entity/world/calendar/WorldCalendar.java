package stonering.entity.world.calendar;

import stonering.game.model.system.ModelComponent;

/**
 * Four seasons are always present, but their names and lengths are different.
 * Season always consists of three months.
 * Names of 'months' are stored here. Months lengths can differ from each other.
 *
 * @author Alexander on 09.12.2019.
 */
public class WorldCalendar implements ModelComponent {
    public final Month[] months;
    public final String[] seasons; // spring, summer, autumn, winter
    public int currentMonth;
    public int currentSeason;

    public WorldCalendar() {
        months = new Month[12];
        seasons = new String[4];
    }


}
