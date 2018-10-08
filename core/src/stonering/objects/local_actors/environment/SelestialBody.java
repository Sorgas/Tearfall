package stonering.objects.local_actors.environment;

import stonering.objects.local_actors.AspectHolder;
import stonering.objects.local_actors.environment.aspects.AbstractLighSourceAspect;

/**
 * Represents sun, moon or other important selestial bodies.
 * Selestial bodies have their phases, changed by {@link Clock}
 *
 * @author Alexander on 07.10.2018.
 */
public class SelestialBody extends AspectHolder {

    protected SelestialBody() {
        super(null); //TODO redesign aspectHolder hierarchy
        addAspect(new AbstractLighSourceAspect(this, true));
    }

    @Override
    public void turn() {
        super.turn();
    }
}
