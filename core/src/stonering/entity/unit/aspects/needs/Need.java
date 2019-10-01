package stonering.entity.unit.aspects.needs;

import stonering.entity.Entity;
import stonering.entity.job.Task;
import stonering.enums.TaskPrioritiesEnum;

/**
 * Abstract class for needs.
 * Need priority should be checked before creating task for performance purposes.
 * TODO refactor to system.
 *
 * @author Alexander Kuzyakov on 21.09.2018.
 */
public abstract class Need {

    /**
     * Returns priority of need. Returns -1 if need can be tolerated.
     */
    public abstract TaskPrioritiesEnum countPriority(Entity entity);

    public abstract Task tryCreateTask(Entity entity);
}
