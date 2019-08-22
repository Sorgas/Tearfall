package stonering.entity.unit.aspects.needs;

import stonering.entity.Entity;
import stonering.entity.job.Task;

/**
 * Need for rest. Generates tasks for:
 *  stop activities on medium exhaustion,
 *  sleeping in a bed,
 *  sleeping at safe place,
 *  sleeping an any place.
 *
 *  Tries to stick to day/night cycle.
 *  //TODO night shift
 *
 * @author Alexander on 22.08.2019.
 */
public class RestNeed extends Need {

    @Override
    public int countPriority(Entity entity) {
        return 0;
    }

    @Override
    public Task tryCreateTask(Entity entity) {
        return null;
    }
}
