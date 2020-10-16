package stonering.game.model.system.task;

import static stonering.enums.action.TaskStatusEnum.*;

import java.util.ArrayList;
import java.util.List;

import stonering.entity.job.Task;

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
    private final List<Task> toRemove = new ArrayList<>();
    private final List<Task> toReopen = new ArrayList<>();

    public TaskStatusSystem(TaskContainer container) {
        this.container = container;
    }

    public void update() {
        toRemove.clear();
        toReopen.clear();
        // canceled unassigned designations removed
        container.tasks.values().stream()
                .flatMap(taskList -> taskList.tasks.stream())
                .filter(task -> task.status == CANCELED)
                .forEach(toRemove::add);
        // canceled and complete assigned designations removed
        container.assignedTasks.stream()
                .filter(task -> task.status == COMPLETE || task.status == CANCELED)
                .forEach(toRemove::add);
        // failed assigned designations recreated
        container.assignedTasks.stream()
                .filter(task -> task.status == FAILED)
                .forEach(toReopen::add);

        toRemove.forEach(task1 -> {
            container.removeTask(task1);
        });

        toReopen.forEach(task -> {
            if (task.designation != null) {
                container.reopenTask(task);
                task.status = OPEN;
            } else {
                container.removeTask(task); // removes task and designation
            }
        });
    }
}