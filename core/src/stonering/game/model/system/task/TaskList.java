package stonering.game.model.system.task;

import static stonering.enums.action.TaskStatusEnum.OPEN;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import stonering.entity.job.Task;
import stonering.enums.time.TimeUnitEnum;
import stonering.util.global.Updatable;
import stonering.util.logging.Logger;

/**
 * List for storing prioritized {@link Task}s.
 * Tasks that were failed, but have designations,
 *
 * @author Alexander on 6/28/2020
 */
public class TaskList implements Updatable {
    public LinkedList<Task> tasks = new LinkedList<>();
    public List<Task> reopenedTasks = new ArrayList<>();

    @Override
    public void update(TimeUnitEnum unit) {
        if(unit == TimeUnitEnum.MINUTE) checkTaskStatuses();

        for (Task task : reopenedTasks) {
            System.out.println(task + " is reopened");
            add(task);
        }
    }

    public void add(Task task) {
        if(tasks.size() == 0) {
            tasks.add(task);
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                if (tasks.get(i).priority <= task.priority) {
                    tasks.add(i, task);
                    return;
                }
            }
        }
    }

    public void addReopenedTask(Task task) {
        reopenedTasks.add(task);
    }

    public boolean remove(Task task) {
        return tasks.remove(task) || reopenedTasks.remove(task);
    }

    private void checkTaskStatuses() {
        for (Task task : tasks) {
            if (task.performer != null) Logger.TASKS.logError("Task " + task + " with performer is in open map.");
            if (task.status != OPEN) Logger.TASKS.logError("Task " + task + " with status " + task.status + " is in open map.");
        }
        for (Task task : reopenedTasks) {
            if (task.performer != null) Logger.TASKS.logError("Reopened task " + task + " with performer is in open map.");
            if (task.status != OPEN) Logger.TASKS.logError("Reopened task " + task + " with status " + task.status + " is in open map.");
        }
    }
}
