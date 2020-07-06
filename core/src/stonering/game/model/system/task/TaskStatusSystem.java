package stonering.game.model.system.task;

import stonering.entity.job.Task;
import stonering.entity.unit.aspects.TaskAspect;
import stonering.game.GameMvc;
import stonering.game.model.system.unit.UnitContainer;
import stonering.util.logging.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static stonering.enums.action.TaskStatusEnum.OPEN;

/**
 * System for handling different statuses of tasks in {@link TaskContainer}.
 * <p>
 * Unassigned tasks should not be ACTIVE or COMPLETE.
 * Unassigned CANCELLED tasks are cancelled by player and removed with their designations.
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
        for (TaskList list : container.tasks.values()) {
            for (Iterator<Task> iterator = list.tasks.iterator(); iterator.hasNext(); ) {
                Task task = iterator.next();
                switch (task.status) {
                    case ACTIVE:
                    case COMPLETE:
                    case FAILED:
                        Logger.TASKS.logError(task + " with status " + task.status + " in unassigned tasks");
                        break;
                    case CANCELED: // remove task canceled by player
                        iterator.remove();
                        if (task.designation != null)
                            container.designations.remove(task.designation.position); // remove designation
                        break;
                }
            }
        }
        for (Iterator<Task> iterator = container.assignedTasks.iterator(); iterator.hasNext(); ) {
            Task task = iterator.next();
            switch (task.status) {
                case OPEN:
                    Logger.TASKS.logError(task.status + " task in assigned tasks");
                    break;
                case COMPLETE: // complete designations are removed by actions
                case CANCELED: // canceled designation are removed in designations system
                    iterator.remove();
                    if (task.designation != null)
                        container.designationSystem.removeDesignation(task.designation.position);
                    break;
                case FAILED: // failed tasks are removed
                    iterator.remove();
                    if (task.designation != null) {
                        System.out.println("reopening task");
                        task.reset();
                        container.addReopenedTask(task);
                    }
//                        task.designation.task = null;
                    break;
            }
        }
    }
}