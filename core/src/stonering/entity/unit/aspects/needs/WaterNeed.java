package stonering.entity.unit.aspects.needs;

import stonering.entity.Entity;
import stonering.entity.job.Task;
import stonering.enums.TaskPrioritiesEnum;

/**
 * @author Alexander on 08.10.2019.
 */
public class WaterNeed extends Need {

    @Override
    public TaskPrioritiesEnum countPriority(Entity entity) {
        return null;
    }

    @Override
    public Task tryCreateTask(Entity entity) {
        return null;
    }
}
