package stonering.entity.local.environment;

import stonering.entity.local.AspectHolder;
import stonering.entity.local.environment.aspects.CelestialLightSource;

import java.io.Serializable;

/**
 * Represents sun, moon or other important celestial bodies.
 * Celestial bodies have their phases, changed by {@link GameCalendar}
 *
 * @author Alexander on 07.10.2018.
 */
public class CelestialBody extends AspectHolder implements Serializable {

    public CelestialBody() {
        super(null); //TODO redesign aspectHolder hierarchy
        addAspect(new CelestialLightSource(this));
    }

    @Override
    public void turn() {
        aspects.forEach((s, aspect) -> aspect.turn());
    }
}
