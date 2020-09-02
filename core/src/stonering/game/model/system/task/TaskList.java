package stonering.game.model.system.task;

import static stonering.enums.action.TaskStatusEnum.FAILED;
import static stonering.enums.action.TaskStatusEnum.OPEN;

import java.util.*;
import java.util.stream.Collectors;

import stonering.entity.job.Task;
import stonering.enums.time.TimeUnitEnum;
import stonering.util.lang.Updatable;
import stonering.util.logging.Logger;

/**
 * List for storing prioritized {@link Task}s.
 * Tasks that were failed, but have designations,
 *
 * @author Alexander on 6/28/2020
 */
public class TaskList implements Updatable {
    public LinkedList<Task> tasks = new LinkedList<>();
    public Map<Task, Integer> reopenedTasks = new HashMap<>(); // task to time counter
    private int delayLimit = 12;

    @Override
    public void update(TimeUnitEnum unit) {
        if (unit != TimeUnitEnum.MINUTE) return;
        checkTaskStatuses();
        rollTimers();
    }

    public void add(Task task) {
        if (tasks.size() == 0) {
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
        reopenedTasks.put(task, 0);
    }

    public boolean remove(Task task) {
        return tasks.remove(task) || reopenedTasks.remove(task) != null;
    }

    private void checkTaskStatuses() {
        for (Task task : tasks) {
            if (task.performer != null) Logger.TASKS.logError("Task " + task + " with performer is in open map.");
            if (task.status != OPEN)
                Logger.TASKS.logError("Task " + task + " with status " + task.status + " is in open map.");
        }
        for (Task task : reopenedTasks.keySet()) {
            if (task.performer != null)
                Logger.TASKS.logError("Reopened task " + task + " with performer is in reopened map.");
            if (task.status != FAILED)
                Logger.TASKS.logError("Reopened task " + task + " with status " + task.status + " is in reopened map.");
        }
    }

    private void rollTimers() {
        List<Task> removed = reopenedTasks.entrySet().stream()
                .peek(entry -> entry.setValue(entry.getValue() + 1))
                .filter(entry -> entry.getValue() >= delayLimit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        removed.forEach(task -> {
            reopenedTasks.remove(task);
            task.status = OPEN;
            tasks.add(task);
        });
    }
}
