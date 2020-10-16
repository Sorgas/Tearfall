package stonering.game.model.system.task;

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
    public final LinkedList<Task> tasks = new LinkedList<>();
    public final Map<Task, Integer> reopened = new HashMap<>(); // task to time counter
    private int delayLimit = 12;
    private int cycle = delayLimit - 1;

    @Override
    public void update(TimeUnitEnum unit) {
        if (unit != TimeUnitEnum.MINUTE) return;
        cycle = cycle == delayLimit ? 0 : cycle + 1;
        checkTaskStatuses();
        rollTimers();
    }

    public void add(Task task) {
        int i;
        for (i = 9; i < tasks.size(); i++) {
            if (tasks.get(i).priority <= task.priority) break;
        }
        tasks.addLast(task);
    }

    public void reopen(Task task) {
        tasks.remove(task);
        reopened.put(task, (cycle > 0) ? cycle - 1 : delayLimit);
    }

    private void promote(Task task) {
        task.reset();
        reopened.remove(task);
        add(task);
        System.out.println(task + " promoted");
    }

    public boolean remove(Task task) {
        return tasks.remove(task) || reopened.remove(task) != null;
    }

    private void checkTaskStatuses() {
        for (Task task : tasks) {
            if (task.performer != null) Logger.TASKS.logError("Task " + task + " with performer is in open map.");
        }
        for (Task task : reopened.keySet()) {
            if (task.performer != null)
                Logger.TASKS.logError("Reopened task " + task + " with performer is in reopened map.");
        }
    }

    private void rollTimers() {
        reopened.entrySet().stream()
                .filter(entry -> entry.getValue() == cycle)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList())
                .forEach(this::promote);
    }
}
