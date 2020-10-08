package stonering.entity.unit.aspects.job;

import stonering.entity.job.Task;
import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.entity.job.action.Action;
import stonering.util.geometry.Position;

/**
 * Holds current creature's {@link Task}.
 * ActionChecked flag is used to reduce number of action checks.
 *
 * @author Alexander Kuzyakov on 10.10.2017.
 */
public class TaskAspect extends Aspect {
    public Task task;
    public boolean actionChecked;

    public TaskAspect(Entity entity) {
        super(entity);
    }

    public Position getTarget() {
        return task != null ? task.nextAction.target.getPosition() : null;
    }

    public Action getNextAction() {
        return task != null ? task.nextAction : null;
    }
}