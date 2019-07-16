package stonering.entity;

import stonering.entity.environment.CelestialBody;
import stonering.game.model.IntervalTurnable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents star system. Updates every in-game minute. Used for deep calendar, eclipses, mood phases (post MVP).
 * //TODO implement
 *
 * @author Alexander Kuzyakov
 */
public class StarSystem extends IntervalTurnable implements Serializable {
    private List<CelestialBody> celestialBodies;

    public StarSystem() {
        celestialBodies = new ArrayList<>();
    }

    public List<CelestialBody> getCelestialBodies() {
        return celestialBodies;
    }

    public void init() {
        celestialBodies.forEach(celestialBody -> celestialBody.init());
    }

    @Override
    public void turnInterval() {
        celestialBodies.forEach(CelestialBody::turn);
    }
}
