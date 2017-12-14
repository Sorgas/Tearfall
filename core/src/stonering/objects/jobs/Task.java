package stonering.objects.jobs;

import stonering.global.utils.Position;

import java.util.ArrayList;

public class Task {
    private String name;
    private TaskStatusesEnum statuses;
    private ArrayList<Action> actions;
    private Position targetPosition;

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
}