package stonering.objects.jobs;

import stonering.game.core.model.lists.TaskContainer;
import stonering.objects.jobs.actions.Action;
import stonering.objects.jobs.actions.TaskTypesEnum;

import java.util.ArrayList;
import java.util.LinkedList;

public class Task {
    private String name;
    private TaskTypesEnum taskType;
    private LinkedList<Action> actions;
    private TaskContainer taskContainer;

    public Task(String name, TaskTypesEnum taskType, TaskContainer taskContainer) {
        this.name = name;
        this.taskType = taskType;
        this.taskContainer = taskContainer;
        actions = new LinkedList<>();
    }

    public void recountFinished() {
        if (actions.isEmpty())
            taskContainer.removeTask(this);
    }

    public Action getNextAction() {
        if (!actions.isEmpty()) {
            return actions.get(0);
        }
        return null;
    }

    public void addFirstAction(Action action) {
        actions.add(0, action);
    }

    public boolean isFinished() {
        return actions.isEmpty();
    }

    public void removeAction(Action action) {
        actions.remove(action);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<Action> getActions() {
        return actions;
    }

    public void setActions(LinkedList<Action> actions) {
        this.actions = actions;
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