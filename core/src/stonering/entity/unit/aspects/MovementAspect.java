package stonering.entity.unit.aspects;

import stonering.entity.Aspect;
import stonering.entity.unit.Unit;
import stonering.util.geometry.Position;

import java.util.List;

/**
 * Holds movement speed, current path, movement status. also builds path.
 *
 * @author Alexander Kuzyakov on 06.10.2017.
 */
public class MovementAspect extends Aspect {
    public Position target; // last target taken from planning aspect
    public List<Position> path; // calculated path

    public int movementDelay;

    public float speed; // part of cell, passed in 1 update
    public float fallSpeed;
    public float stepProgress;

    public MovementAspect(Unit unit) {
        super(unit);
        this.entity = unit;
        stepProgress = 0;
    }
}