package stonering.objects.aspects;

import stonering.objects.local_actors.units.Unit;
import stonering.global.utils.Position;

import java.util.ArrayList;

/**
 * Created by Alexander on 10.10.2017.
 */
public class PlanningAspect extends Aspect {
    private ArrayList<Position> route;

    public PlanningAspect(Unit unit) {
        this.unit = unit;
        this.name="planning";
    }

    public Position getStep() {
        if(route.size() > 0) {
        return route.get(0);}else {
            return unit.getPosition();
        }
    }

    public void poll() {
        if (route.size() > 0) route.remove(0);
    }
}
