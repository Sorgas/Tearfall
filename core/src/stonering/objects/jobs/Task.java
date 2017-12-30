package stonering.objects.jobs;

import stonering.objects.jobs.actions.Action;
import stonering.objects.jobs.actions.TaskTypesEnum;

import java.util.ArrayList;

public class Task {
    private String name;
    private TaskTypesEnum taskType;
    private ArrayList<Action> actions;
    private boolean finished;

    public Task(String name, TaskTypesEnum taskType) {
        this.name = name;
        this.taskType = taskType;
        actions = new ArrayList<>();
    }

    public void recountFinished() {
        for (Action action : actions) {
            if (!action.isFinished()) {
                finished = false;
                return;
            }
        }
        finished = true;
    }

    public Action getNextAction() {
        if (!finished) {
            for (Action action : actions) {
                if (!action.isFinished()) {
                    return action;
                }
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public void setActions(ArrayList<Action> actions) {
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

    public boolean isFinished() {
        return finished;
    }
}