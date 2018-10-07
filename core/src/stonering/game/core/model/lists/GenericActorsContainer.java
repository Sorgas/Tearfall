package stonering.game.core.model.lists;

import stonering.game.core.model.Turnable;
import stonering.objects.local_actors.environment.Clock;

/**
 * @author Alexander on 07.10.2018.
 */
public class GenericActorsContainer implements Turnable {
    private Clock clock;

    public GenericActorsContainer() {
        clock = new Clock();
    }

    public void init() {
        clock.init();
    }

    @Override
    public void turn() {
        clock.turn();
    }
}
