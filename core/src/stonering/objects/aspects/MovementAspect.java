package stonering.objects.aspects;

import stonering.game.core.model.LocalMap;
import stonering.objects.local_actors.unit.Unit;
import stonering.global.utils.Position;

import java.util.Random;

/**
 * Created by Alexander on 06.10.2017.
 *
 * Holds movement speed, current path, movement status. also builds path.
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
        stepTime = 15;
        stepDelay = new Random().nextInt(stepTime);
    }

    public void recountSpeed() {

    }

    public void init() {
        if(unit.getAspects().containsKey("planning"))
        planning = (PlanningAspect) unit.getAspects().get("planning");
        map = unit.getLocalMap();
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