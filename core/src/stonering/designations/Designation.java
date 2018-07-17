package stonering.designations;

import stonering.enums.designations.DesignationTypes;
import stonering.global.utils.Position;
import stonering.objects.jobs.Task;

/**
 * @author Alexander Kuzyakov on 26.06.2018
 */
public abstract class Designation {
    protected Position position;
    protected Task task;
    private DesignationTypes type;

    public Designation(Position position, DesignationTypes type) {
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

    public DesignationTypes getType() {
        return type;
    }
}