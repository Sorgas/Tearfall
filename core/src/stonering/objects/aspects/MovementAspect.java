package stonering.objects.aspects;

import stonering.game.core.model.LocalMap;
import stonering.objects.local_actors.units.Unit;
import stonering.global.utils.Position;

/**
 * Created by Alexander on 06.10.2017.
 */
public class MovementAspect extends Aspect {
    private int stepTime;
    private int stepDelay;
    private Unit unit;
    private LocalMap map;
    private PlanningAspect planning;

    public MovementAspect(Unit unit) {
        this.name = "movement";
        this.unit = unit;
    }

    public void recountSpeed() {

    }

    public void init() {
        if(unit.getAspects().containsKey("planning"))
        planning = (PlanningAspect) unit.getAspects().get("planning");
    }

    public void move() {
        if (stepDelay == 0) {
            if (planning != null) {
                Position nextPosition = planning.getStep();
                if (map.isPassable(nextPosition.getX(), nextPosition.getY(), nextPosition.getZ())) {
                    stepDelay = stepTime;
                    unit.setPosition(nextPosition.clone());
                    planning.poll();
                } else {
                    // drop route
                }
            }
        } else {
            stepDelay--;
        }
    }
}