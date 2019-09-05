package stonering.entity.unit.aspects.needs;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.entity.job.Task;
import stonering.entity.unit.aspects.needs.NeedEnum;
import stonering.game.model.system.units.CreatureNeedSystem;

import java.util.ArrayList;

/**
 * Holds names of creature's needs. Needs updated in a system {@link CreatureNeedSystem}.
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
