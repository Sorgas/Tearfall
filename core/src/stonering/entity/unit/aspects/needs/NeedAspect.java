package stonering.entity.unit.aspects.needs;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.entity.job.Task;
import stonering.entity.unit.aspects.health.NeedState;
import stonering.game.model.system.unit.NeedTaskSystem;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores states of creature's needs. Needs updated in a {@link NeedTaskSystem}.
 * Stores task for satisfying creature strongest need.
 *
 * @author Alexander Kuzyakov on 16.09.2018.
 */
public class NeedAspect extends Aspect {
    public Task satisfyingTask; // taken by planning
    public final Map<NeedEnum, NeedState> needs; // creature needs

    public NeedAspect(Entity entity) {
        super(entity);
        needs = new HashMap<>();
    }
}
