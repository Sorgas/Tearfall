package stonering.game.core.model.lists;

import stonering.game.core.model.GameContainer;
import stonering.game.core.model.IntervalTurnable;
import stonering.generators.localgen.LocalGenContainer;
import stonering.objects.local_actors.environment.SelestialBody;

import java.util.ArrayList;

/**
 * Container for actors which make turns once in a some period of time like gods, fractions, weather, etc.
 *
 * @author Alexander on 07.10.2018.
 */
public class GlobalActorsContainer extends IntervalTurnable {

    private GameContainer container;
    private ArrayList<IntervalTurnable> actors;

    public GlobalActorsContainer(GameContainer container) {
        this.container = container;
    }

    public void init(LocalGenContainer container) {
        container.getSelestialBodies().forEach(this::addSelestialBody);
    }

    private void addSelestialBody(SelestialBody selestialBody) {

    }

    @Override
    public void turnMinute() {
        actors.forEach(IntervalTurnable::turnMinute);
    }

    @Override
    public void turnHour() {
        actors.forEach(IntervalTurnable::turnHour);
    }

    @Override
    public void turnDay() {
        actors.forEach(IntervalTurnable::turnDay);
    }

    @Override
    public void turnMonth() {
        actors.forEach(IntervalTurnable::turnMonth);
    }

    @Override
    public void turnYear() {
        actors.forEach(IntervalTurnable::turnYear);
    }
}
