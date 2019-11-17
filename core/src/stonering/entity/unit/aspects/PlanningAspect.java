package stonering.entity.unit.aspects;

import stonering.entity.job.Task;
import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.util.geometry.Position;

/**
 * Holds current creature's task. Task itself, is a sequence of actions with pointer to active action.
 * Has flag for indicating whether movement is needed for performing task.
 * Flag is false if task is null, or target of an action is reached.
 *
 * @author Alexander Kuzyakov on 10.10.2017.
 */
public class PlanningAspect extends Aspect {
    public Task task;
    public boolean movementNeeded;
    public boolean actionChecked;

    public PlanningAspect(Entity entity) {
        super(entity);
    }

    public Position getTarget() {
        return task != null ? task.nextAction.actionTarget.getPosition() : null;
    }
}