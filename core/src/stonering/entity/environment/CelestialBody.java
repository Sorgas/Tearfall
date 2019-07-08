package stonering.entity.environment;

import stonering.entity.Entity;
import stonering.entity.environment.aspects.CelestialLightSourceAspect;

import java.io.Serializable;

/**
 * Represents sun, moon or other important celestial bodies.
 * Celestial bodies have their phases, changed by {@link stonering.entity.environment.GameCalendar}
 *
 * @author Alexander on 07.10.2018.
 */
public class CelestialBody extends Entity implements Serializable {

    public CelestialBody() {
        super(null); //TODO redesign entity hierarchy
        addAspect(new CelestialLightSourceAspect(this));
    }

    @Override
    public void turn() {
        aspects.forEach((s, aspect) -> aspect.turn());
    }
}