package stonering.designations;

import stonering.global.utils.Position;
import stonering.objects.jobs.Task;

/**
 * @author Alexander Kuzyakov on 26.06.2018
 */
public abstract class Designation {
    protected Position position;
    protected Task task;

    public Designation(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
