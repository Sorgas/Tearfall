package stonering.objects.jobs;

import stonering.enums.ProfessionsEnum;
import stonering.game.core.model.GameContainer;
import stonering.game.core.model.lists.TaskContainer;
import stonering.global.utils.Position;
import stonering.global.utils.pathfinding.NoPathException;
import stonering.global.utils.pathfinding.a_star.AStar;
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

    public boolean isTaskTargetsAvaialbleFrom(Position position) {
        AStar aStar = new AStar(container.getLocalMap());
        if (aStar.makeShortestPath(position, initialAction.getTargetPosition()) == null) {
            return false;
        }
        for (Action action : actions) {
            if (aStar.makeShortestPath(position, action.getTargetPosition()) == null) {
                return false;
            }
        }
        return true;
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
}