package stonering.entity.environment;

import stonering.entity.Entity;
import stonering.entity.environment.aspects.CelestialLightSourceAspect;
import stonering.game.model.system.GameTime;

import java.io.Serializable;

/**
 * Represents sun, moon or other important celestial bodies.
 * Celestial bodies have their phases, changed by {@link GameTime}
 *
 * @author Alexander on 07.10.2018.
 */
public class CelestialBody extends Entity implements Serializable {

    public CelestialBody() {
        //TODO redesign entity hierarchy
    }
}
