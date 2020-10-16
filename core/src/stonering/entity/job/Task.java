package stonering.entity.job;

import stonering.entity.item.Item;
import stonering.entity.job.action.target.ActionTarget;
import stonering.entity.job.designation.Designation;
import stonering.game.model.system.item.ItemContainer;
import stonering.entity.job.action.Action;
import stonering.entity.unit.Unit;
import stonering.enums.action.TaskStatusEnum;
import stonering.util.logging.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static stonering.enums.action.ActionStatusEnum.COMPLETE;
import static stonering.enums.action.TaskStatusEnum.*;

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
    public TaskStatusEnum status = OPEN;
    public Designation designation;                             // some tasks are displayed on map (e.g. digging)
    public Unit performer;
    public String job;                                          // used to filter tasks, when task is selected for unit
    public int priority = 1;                                    // Unit selects task with max priority (e.g. labor vs needs)
    public final List<Item> lockedItems = new ArrayList<>();

    public final Action initialAction;
    private final LinkedList<Action> preActions = new LinkedList<>();
    private final LinkedList<Action> postActions = new LinkedList<>();
    public Action nextAction; // points to first action in whole task

    public Task(Action initialAction, String job, int priority) {
        this.initialAction = initialAction;
        this.job = job != null ? job : "none";
        this.priority = priority;
        initialAction.task = this;
        updateNextAction();
    }

    public Task(Action initialAction, int priority) {
        this(initialAction, null, priority);
    }

    public Task(Action initialAction, String job) {
        this(initialAction, job, 1);
    }

    public Task(Action initialAction) {
        this(initialAction, null, 1);
    }

    public void reset() {
//        Logger.TASKS.logDebug("Resetting " + this);
        initialAction.reset();
        preActions.clear();
        postActions.clear();
        updateNextAction();
        performer = null;
    }

    /**
     * Task is finished, if initial action is finished, and no other action remain.
     */
    public boolean isFinished() {
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

    private void actionAdded(Action action) {
        action.task = this;
        updateNextAction();
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
        return initialAction.toString();
    }

    public ActionTarget initialTarget() {
        return initialAction.target;
    }
}