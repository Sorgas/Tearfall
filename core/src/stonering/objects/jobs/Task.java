package stonering.objects.jobs;

import stonering.designations.Designation;
import stonering.game.core.model.GameContainer;
import stonering.game.core.model.lists.TaskContainer;
import stonering.global.utils.Position;
import stonering.objects.jobs.actions.Action;
import stonering.objects.jobs.actions.TaskTypesEnum;
import stonering.objects.local_actors.unit.Unit;

import java.util.LinkedList;

public class Task {
    private String name;
    private Unit performer;
    private TaskTypesEnum taskType;
    private Action initialAction;
    private LinkedList<Action> actions;
    private TaskContainer taskContainer;
    private GameContainer container;
    private Designation designation;

    public Task(String name, TaskTypesEnum taskType, Action initialAction, TaskContainer taskContainer, GameContainer container) {
        this.name = name;
        this.taskType = taskType;
        this.initialAction = initialAction;
        initialAction.setTask(this);
        this.taskContainer = taskContainer;
        actions = new LinkedList<>();
        this.container = container;
    }

    public void recountFinished() {
        if (isFinished())
            taskContainer.removeTask(this);
    }

    public Action getNextAction() {
        if (!actions.isEmpty()) {
            return actions.get(0);
        } else {
            return initialAction;
        }
    }

    public void setPerformer(Unit performer) {
        this.performer = performer;
        initialAction.setPerformer(performer);
        actions.forEach((action) -> action.setPerformer(performer));
    }

    public void reset() {
        actions = new LinkedList<>();
        setPerformer(null);
    }

    public void addFirstAction(Action action) {
        actions.add(0, action);
        action.setTask(this);
    }

    public boolean isFinished() {
        return actions.isEmpty() && initialAction.isFinished();
    }

    public void removeAction(Action action) {
        if (action != initialAction) {
            actions.remove(action);
        }
    }

    public void fail() {
        taskContainer.removeTask(this);
    }

    public boolean isTaskTargetsAvaialbleFrom(Position position) {
        int sourceArea = container.getLocalMap().getArea(position);
        Position target = initialAction.getTargetPosition();
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                if(x != 0 && y != 0
                        && container.getLocalMap().getArea(target.getX() + x, target.getY() + y, target.getZ()) == sourceArea) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addAction(Action action) {
        actions.add(action);
        action.setPerformer(performer);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaskTypesEnum getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskTypesEnum taskType) {
        this.taskType = taskType;
    }

    public Unit getPerformer() {
        return performer;
    }

    public Action getInitialAction() {
        return initialAction;
    }

    public void setInitialAction(Action initialAction) {
        this.initialAction = initialAction;
    }

    public Designation getDesignation() {
        return designation;
    }

    public void setDesignation(Designation designation) {
        this.designation = designation;
    }
}