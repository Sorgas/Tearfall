package stonering.entity.zone.aspect;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.entity.item.selectors.SeedItemSelector;
import stonering.entity.job.Task;
import stonering.util.geometry.Position;

/**
 * Contains all data for farm zones.
 * 
 * @author Alexander on 13.07.2020.
 */
public class FarmAspect extends Aspect {
    public final Set<String> plantTypes = new HashSet<>();
    public SeedItemSelector seedSelector; // planting tasks share this selector. it is updated as months change.
    public Map<Position, Task> taskMap; // farm stores its tasks in this map to avoid committing duplicating tasks to container. tasks are removed when finished.
    
    public FarmAspect(Entity entity) {
        super(entity);
    }
}
