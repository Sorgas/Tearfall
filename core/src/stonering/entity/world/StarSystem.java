package stonering.entity.world;

import stonering.entity.environment.CelestialBody;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.model.Turnable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents star system. Updates every in-game minute. Used for deep calendar, eclipses, mood phases (post MVP).
 * //TODO implement
 *
 * @author Alexander Kuzyakov
 */
public class StarSystem implements Serializable, Turnable {
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
    public void turn() {
        celestialBodies.forEach(CelestialBody::turn);
    }
}
