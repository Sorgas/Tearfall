package stonering.entity.job.designation;

import static stonering.enums.action.TaskStatusEnum.CANCELED;
import static stonering.enums.action.TaskStatusEnum.COMPLETE;

import stonering.entity.Entity;
import stonering.entity.RenderAspect;
import stonering.enums.action.TaskStatusEnum;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.model.system.task.TaskContainer;
import stonering.stage.renderer.AtlasesEnum;
import stonering.util.geometry.Position;
import stonering.entity.job.Task;

/**
 * Class for storing in {@link TaskContainer} to be rendered on local map.
 * Has its position, type for tile choosing and task.
 * Task is created in {@link stonering.game.model.system.task.DesignationSystem}
 *
 * @author Alexander Kuzyakov on 26.06.2018
 */
public abstract class Designation extends Entity {
    public DesignationTypeEnum type;
    public Task task;

    public Designation(Position position, DesignationTypeEnum type) {
        super(position);
        this.type = type;
        add(new RenderAspect(this, 0, 4, AtlasesEnum.ui_tiles)); // default tile for designations
    }

    /**
     * Designation is finished if its task status is COMPLETE or CANCELED.
     */
    public boolean isFinished() {
        return task != null && (task.status == CANCELED || task.status == COMPLETE);
    }
}
