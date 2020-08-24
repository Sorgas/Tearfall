package stonering.entity.unit.aspects.need;

import static stonering.enums.action.TaskStatusEnum.COMPLETE;
import static stonering.enums.action.TaskStatusEnum.FAILED;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.entity.job.Task;
import stonering.enums.unit.need.NeedEnum;
import stonering.game.model.system.unit.NeedSystem;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores states of creature's needs. Needs updated in a {@link NeedSystem}.
 * Stores task for satisfying creature strongest need.
 *
 * @author Alexander Kuzyakov on 16.09.2018.
 */
public class NeedAspect extends Aspect {
    public final Map<NeedEnum, NeedState> needs; // creature needs
    public Task satisfyingTask; // taken by planning

    public NeedAspect(Entity entity) {
        super(entity);
        needs = new HashMap<>();
    }
    
    public boolean canAcceptTask() {
        return satisfyingTask == null || satisfyingTask.status == FAILED || satisfyingTask.status == COMPLETE;
    }
}
