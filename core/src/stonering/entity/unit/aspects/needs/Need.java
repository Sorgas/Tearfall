package stonering.entity.unit.aspects.needs;

import stonering.entity.job.Task;
import stonering.entity.unit.Unit;
import stonering.enums.action.TaskPriorityEnum;

/**
 * Abstract class for needs.
 * Need priority should be checked before creating task for performance purposes.
 *
 * @author Alexander Kuzyakov on 21.09.2018.
 */
public abstract class Need {

    /**
     * Returns priority of need. Returns -1 if need can be tolerated.
     */
    public abstract TaskPriorityEnum countPriority(Unit unit);

    public abstract Task tryCreateTask(Unit unit);

}
