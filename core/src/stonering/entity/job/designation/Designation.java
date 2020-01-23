package stonering.entity.job.designation;

import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.model.system.task.TaskContainer;
import stonering.util.geometry.Position;
import stonering.entity.job.Task;

/**
 * Class for storing in {@link TaskContainer} to be rendered on local map.
 * Has its position, type for tile choosing and task.
 *
 * @author Alexander Kuzyakov on 26.06.2018
 */
public abstract class Designation {
    public final Position position;
    public Task task;
    public DesignationTypeEnum type;

    public Designation(Position position, DesignationTypeEnum type) {
        this.position = position;
        this.type = type;
    }
}
