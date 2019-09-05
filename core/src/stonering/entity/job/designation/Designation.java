package stonering.entity.job.designation;

import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.model.system.tasks.TaskContainer;
import stonering.util.geometry.Position;
import stonering.entity.job.Task;

/**
 * Class for storing in {@link TaskContainer} to be rendered on local map.
 * Has its position, type for tile choosing and task.
 *
 * @author Alexander Kuzyakov on 26.06.2018
 */
public abstract class Designation {
    protected Position position;
    protected Task task;
    private DesignationTypeEnum type;

    public Designation(Position position, DesignationTypeEnum type) {
        this.position = position;
        this.type = type;
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

    public DesignationTypeEnum getType() {
        return type;
    }
}
