package stonering.entity.unit.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;
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
    public float speed = 0.03f;

    public MovementAspect(Entity entity) {
        super(entity);
    }

    /**
     * Deletes target and path making unit to stop immediately.
     */
    public void reset() {
        path = null;
        target = null;
    }
}