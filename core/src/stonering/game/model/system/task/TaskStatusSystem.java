package stonering.game.model.system.task;

import stonering.entity.job.Task;

/**
 * System for handling different statuses of tasks.
 * Removes completed tasks,
 *
 * @author Alexander on 01.11.2019.
 */
public class TaskStatusSystem {
    private TaskContainer container;

    public TaskStatusSystem(TaskContainer container) {
        this.container = container;
    }

    public void update() {
    }

    public void handleFail(Task task) {

    }

    public void handleComplete(Task task) {

    }
}
