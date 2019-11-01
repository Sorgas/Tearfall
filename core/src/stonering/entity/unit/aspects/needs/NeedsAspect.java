package stonering.entity.unit.aspects.needs;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.entity.job.Task;
import stonering.game.model.system.unit.CreatureNeedSystem;

import java.util.ArrayList;

/**
 * Stores names of creature's needs. Needs updated in a {@link CreatureNeedSystem}.
 * Stores task for satisfying creature strongest need.
 *
 * @author Alexander Kuzyakov on 16.09.2018.
 */
public class NeedsAspect extends Aspect {
    public final ArrayList<NeedEnum> needs; // need names
    public Task satisfyingTask; // taken by planning

    public NeedsAspect(Entity entity) {
        super(entity);
        needs = new ArrayList<>();
    }
}
