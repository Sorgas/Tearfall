package stonering.objects.local_actors.unit.aspects;

import stonering.game.core.model.GameContainer;
import stonering.game.core.model.LocalMap;
import stonering.objects.local_actors.unit.Unit;
import stonering.global.utils.Position;

import java.util.Random;

/**
 * Created by Alexander on 06.10.2017.
 * <p>
 * Holds movement speed, current path, movement status. also builds path.
 */
public class MovementAspect extends Aspect {
    private int stepTime;
    private int stepDelay;
    private LocalMap map;
    private PlanningAspect planning;

    public MovementAspect(Unit unit) {
        this.name = "movement";
        this.unit = unit;
        stepTime = 15;
        stepDelay = new Random().nextInt(stepTime);
    }

    public void turn() {
        if (stepDelay > 0) {
            stepDelay--; //counting ticks to step
        } else {
            if (planning != null) {
                Position nextPosition = planning.getStep();
                if (map.isPassable(nextPosition)) {
                    unit.setPosition(nextPosition.clone()); //step
                    stepDelay = stepTime;
                } else {
                    planning.dropRoute(); //path blocked
                }
            } else {
                //no planning aspect
            }
        }
    }

    @Override
    public void init(GameContainer gameContainer) {
        if (unit.getAspects().containsKey("planning"))
            planning = (PlanningAspect) unit.getAspects().get("planning");
        map = gameContainer.getLocalMap();
    }
}