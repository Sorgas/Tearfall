package stonering.objects.local_actors.environment;

import stonering.objects.local_actors.AspectHolder;
import stonering.objects.local_actors.environment.aspects.SelestialLightSource;

/**
 * Represents sun, moon or other important celestial bodies.
 * Celestial bodies have their phases, changed by {@link GameCalendar}
 *
 * @author Alexander on 07.10.2018.
 */
public class CelestialBody extends AspectHolder {

    public CelestialBody() {
        super(null); //TODO redesign aspectHolder hierarchy
        addAspect(new SelestialLightSource(this));
    }

    @Override
    public void turn() {
        super.turn();
    }
}
