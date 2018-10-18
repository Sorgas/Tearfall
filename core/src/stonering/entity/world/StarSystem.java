package stonering.entity.world;

import stonering.entity.local.environment.CelestialBody;
import stonering.game.core.model.IntervalTurnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents star system. Updates every in-game minute.
 * //TODO implement
 *
 * @author Alexander Kuzyakov
 */
public class StarSystem extends IntervalTurnable {
    private List<CelestialBody> celestialBodies;

    public StarSystem() {
        celestialBodies = new ArrayList<>();
    }

    public List<CelestialBody> getCelestialBodies() {
        return celestialBodies;
    }

    @Override
    public void turnMinute() {
        celestialBodies.forEach(IntervalTurnable::turnMinute);
    }
}
