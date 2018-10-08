package stonering.game.core.model.lists;

import stonering.game.core.model.GameContainer;
import stonering.game.core.model.Turnable;
import stonering.generators.localgen.LocalGenContainer;
import stonering.objects.local_actors.environment.Calendar;
import stonering.objects.local_actors.environment.SelestialBody;

/**
 * Container for actors like gods, fractions, weather, etc.
 * These actors have their own time scale, and are invoked by {@link Calendar}
 *
 * @author Alexander on 07.10.2018.
 */
public class GlobalActorsContainer implements Turnable {
    private GameContainer container;
    private Calendar calendar;
    private

    public GlobalActorsContainer(GameContainer container) {
        this.container = container;
        calendar = new Calendar();
    }

    public void init(LocalGenContainer container) {
        container.getSelestialBodies().forEach(this::addSelestialBody);
        calendar.init();
    }

    private void addSelestialBody(SelestialBody selestialBody) {

    }

    @Override
    public void turn() {
        calendar.turn();
    }
}
