package stonering.game.core.model.lists;

import stonering.game.core.model.MainGameModel;
import stonering.game.core.model.IntervalTurnable;
import stonering.generators.localgen.LocalGenContainer;
import stonering.entity.local.environment.CelestialBody;

import java.util.ArrayList;

/**
 * Container for actors which make turns once in a some period of time like gods, fractions, weather, etc.
 *
 * @author Alexander Kuzyakov on 07.10.2018.
 */
public class GlobalActorsContainer extends IntervalTurnable {

    private MainGameModel container;
    private ArrayList<CelestialBody> celestialBodies;
    private ArrayList<IntervalTurnable> actors;

    public GlobalActorsContainer(MainGameModel container) {
        this.container = container;
    }

    public void init(LocalGenContainer container) {
        celestialBodies.addAll(container.getWorld().getStarSystem().getCelestialBodies());
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
