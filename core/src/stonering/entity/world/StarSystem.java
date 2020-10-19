package stonering.entity.world;

import stonering.entity.environment.CelestialBody;
import stonering.util.lang.Updatable;
import stonering.game.model.system.EntityContainer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents star system. Updates every in-game minute. Used for deep calendar, eclipses, mood phases (post MVP).
 * //TODO implement
 *
 * @author Alexander Kuzyakov
 */
public class StarSystem extends EntityContainer<CelestialBody> implements Serializable, Updatable {
    public final Map<String, CelestialBody> bodies = new HashMap<>();
    public final CelestialBody mainPlanet; // main planet
    public final CelestialBody mainStar; // main star

    public StarSystem() {
        mainPlanet = new CelestialBody();
        mainStar = new CelestialBody();
    }
}
