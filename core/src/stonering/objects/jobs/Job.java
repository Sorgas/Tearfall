package stonering.objects.jobs;

import java.util.ArrayList;

public class Job {
    private String name;
    private TaskStatusesEnum statuses;
    private ArrayList<Task> tasks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaskStatusesEnum getStatuses() {
        return statuses;
    }

    public void setStatuses(TaskStatusesEnum statuses) {
        this.statuses = statuses;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public Task getNextTask() {
            return tasks.get(0);

    }
}
