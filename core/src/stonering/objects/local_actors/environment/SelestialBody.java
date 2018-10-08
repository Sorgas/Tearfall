package stonering.objects.local_actors.environment;

import stonering.objects.local_actors.AspectHolder;
import stonering.objects.local_actors.environment.aspects.LighSourceAspect;

/**
 * @author Alexander on 07.10.2018.
 */
public class SelestialBody extends AspectHolder {


    protected SelestialBody() {
        super(null); //TODO redesign aspectHolder hierarchy
        addAspect(new LighSourceAspect(this, true));
    }

    @Override
    public void turn() {
        super.turn();

    }
}
