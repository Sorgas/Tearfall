package stonering.objects.aspects;

import stonering.objects.local_actors.unit.Unit;

/**
 * Created by Alexander on 10.10.2017.
 */
public abstract class Aspect {
    protected String name;
    protected Unit unit;

    public String getName() {
        return name;
    }

    public Unit getUnit() {
        return unit;
    }

    public abstract void init();
}
