package stonering.entity.job;

import stonering.entity.item.Item;
import stonering.entity.job.designation.Designation;
import stonering.enums.unit.job.JobsEnum;
import stonering.game.model.system.item.ItemContainer;
import stonering.entity.job.action.Action;
import stonering.entity.unit.Unit;
import stonering.enums.action.TaskStatusEnum;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static stonering.enums.action.ActionStatusEnum.COMPLETE;
import static stonering.enums.unit.job.JobsEnum.NONE;

/**
 * Task object for unit behavior in the game.
 * Basically is a sequence of {@link Action}s, organized into:
 * 1. preActions - performed before initial action,
 * 2. initialAction,
 * 3. postActions - performed after initial action.
 * <p>
 * Created with one action as initial. Actions are checked before performing, and can create additional pre- and post- actions.
 * Actions can consume some items during performing, this items are locked, when action is successfully checked first time.
 * Locked items are stored in task and {@link ItemContainer}.
 *
 * @author Alexander Kuzyakov
 */
public class Task {
    public final String name;
    public Unit performer;
    public Designation designation; // some tasks are displayed on map (e.g. digging)
    public TaskStatusEnum status; // TaskContainer uses this
    public JobsEnum job; // used to filter tasks, when task is selected for unit
    public final List<Item> lockedItems;
    public int priority; // Unit selects task with max priority (e.g. labor vs needs)

    public final Action initialAction;
    private final LinkedList<Action> preActions = new LinkedList<>();
    private final LinkedList<Action> postActions = new LinkedList<>();
    public Action nextAction; // points to first action in whole task

    public Task(String name, Action initialAction, int priority) {
        this.name = name;
        this.initialAction = initialAction;
        this.priority = priority;
        initialAction.task = this;
        status = TaskStatusEnum.OPEN;
        job = NONE;
        lockedItems = new ArrayList<>();
        updateNextAction();
    }

    /**
     * Resets this task to the state after creation.
     */
    public void reset() {
        Logger.TASKS.logDebug("Resetting task " + this);
        preActions.clear();
        postActions.clear();
        performer = null;
    }

    /**
     * Task is finished, if initial action is finished, and no other action remain.
     */
    public boolean isNoActionsLeft() {
        return preActions.isEmpty() && initialAction.status == COMPLETE && postActions.isEmpty();
    }

    public void removeAction(Action action) {
        preActions.remove(action);
        postActions.remove(action);
        updateNextAction();
    }

    public Action addFirstPreAction(Action action) {
        preActions.add(0, action);
        actionAdded(action);
        return action;
    }

    public void addLastPreAction(Action action) {
        preActions.add(action);
        actionAdded(action);
    }

    public void addFirstPostAction(Action action) {
        postActions.add(0, action);
        actionAdded(action);
    }

    public void addLastPostAction(Action action) {
        postActions.add(action);
        actionAdded(action);
    }

    private void actionAdded(Action action) {
//        Logger.TASKS.logDebug("Action " + action + " added to task " + name);
        action.task = this;
        updateNextAction();
//        Logger.TASKS.logDebug("Actions count pre: " + preActions.size() + " post: " + postActions.size());
//        Logger.TASKS.logDebug("Next action is: " + nextAction);
    }

    /**
     * Searches next action to perform and sets nextAction field.
     */
    private void updateNextAction() {
        if (!postActions.isEmpty()) nextAction = postActions.get(0);
        if (initialAction.status != COMPLETE) nextAction = initialAction;
        if (!preActions.isEmpty()) nextAction = preActions.get(0);
    }

    @Override
    public String toString() {
        return name;
    }
}