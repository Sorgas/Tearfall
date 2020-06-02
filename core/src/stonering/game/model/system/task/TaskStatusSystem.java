package stonering.game.model.system.task;

import stonering.entity.job.Task;
import stonering.entity.unit.aspects.TaskAspect;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.util.global.Logger;

import java.util.Iterator;
import java.util.List;

import static stonering.enums.action.TaskStatusEnum.OPEN;

/**
 * System for handling different statuses of tasks in {@link TaskContainer}.
 * <p>
 * Unassigned tasks should not be ACTIVE or COMPLETE.
 * Unassigned FAILED tasks are cancelled by player and removed with their designations.
 * <p>
 * Assigned tasks should not be OPEN.
 * Assigned COMPLETE and CANCELED tasks are removed with their designations.
 * Assigned FAILED tasks are reset to be taken later, when they probably could be performed.
 *
 * @author Alexander on 01.11.2019.
 */
public class TaskStatusSystem {
    private TaskContainer container;

    public TaskStatusSystem(TaskContainer container) {
        this.container = container;
    }

    public void update() {
        for (Iterator<Task> iterator = container.assignedTasks.iterator(); iterator.hasNext(); ) {
            Task task = iterator.next();
            switch (task.status) {
                case OPEN:
                    Logger.TASKS.logError(task.status + " task in assigned tasks");
                    break;
                case COMPLETE: // complete designations are removed
                case CANCELED:
                    iterator.remove();
                    if (task.designation != null) container.designations.remove(task.designation.position); // remove designation
                    break;
                case FAILED: // failed tasks are reset to be taken again
                    iterator.remove();
                    if (task.designation != null) { // designation tasks are reopened
//                        if(task.designation.type == DesignationTypeEnum.D_BUILD) {
//                            task.suspend
//                        }
                        // suspend order
                        task.performer.get(TaskAspect.class).task = null;
                        task.reset();
                        task.status = OPEN;
                        container.addTask(task);
                    }
                    break;
            }
        }
        for (List<Task> value : container.tasks.values()) {
            for (Iterator<Task> iterator = value.iterator(); iterator.hasNext(); ) {
                Task task = iterator.next();
                switch (task.status) {
                    case ACTIVE:
                    case COMPLETE:
                    case FAILED:
                        Logger.TASKS.logError(task.status + " task in unassigned tasks");
                        break;
                    case CANCELED:
                        iterator.remove();
                        if (task.designation != null) container.designations.remove(task.designation.position); // remove designation
                        break;
                }
            }
        }
    }
}