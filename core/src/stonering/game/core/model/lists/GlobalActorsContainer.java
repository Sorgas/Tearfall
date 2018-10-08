package stonering.game.core.model.lists;

import stonering.game.core.model.Turnable;
import stonering.objects.local_actors.environment.Calendar;

/**
 * Container for actors like gods, fractions, weather, etc.
 * These actors have their own time scale, and are invoked by {@link Calendar}
 *
 * @author Alexander on 07.10.2018.
 */
public class GlobalActorsContainer implements Turnable {
    private Calendar calendar;


    public GlobalActorsContainer() {
        calendar = new Calendar();
    }

    public void init() {
        calendar.init();
    }

    @Override
    public void turn() {
        calendar.turn();
    }
}
