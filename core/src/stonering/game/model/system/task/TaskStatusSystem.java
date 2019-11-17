package stonering.game.model.system.task;

import stonering.entity.job.Task;
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
 * Assigned COMPLETE tasks are removed with their designations.
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
                    Logger.TASKS.logError("open task in assigned tasks");
                    break;
                case COMPLETE: // complete designations are removed
                    iterator.remove();
                    if (task.designation != null) container.designationSystem.removeDesignation(task.designation);
                    break;
                case FAILED: // failed designations are reset to be taken again
                    iterator.remove();
                    if (task.designation != null) {
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
                        Logger.TASKS.logError(task.status + " task in assigned tasks");
                        break;
                    case FAILED: // tasks are canceled by player via failed status
                        iterator.remove();
                        if (task.designation != null) container.designationSystem.removeDesignation(task.designation);
                        break;
                }
            }
        }
    }
}