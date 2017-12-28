package stonering.objects.jobs;

import stonering.objects.jobs.actions.Action;
import stonering.objects.jobs.actions.TaskTypesEnum;

import java.util.ArrayList;

public class Task {
    private String name;
    private TaskTypesEnum taskType;
    private TaskStatusesEnum statuses;
    private ArrayList<Action> actions;

    public Task(String name, TaskTypesEnum taskType) {
        this.name = name;
        this.taskType = taskType;
        actions = new ArrayList<>();
    }

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

    public ArrayList<Action> getActions() {
        return actions;
    }

    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }

    public Action getNextAction() {
        return actions.get(0);
    }

    public TaskTypesEnum getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskTypesEnum taskType) {
        this.taskType = taskType;
    }

    public void addAction(Action action) {
        actions.add(action);
    }
}