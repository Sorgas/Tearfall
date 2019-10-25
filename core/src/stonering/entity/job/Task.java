package stonering.entity.job;

import stonering.entity.job.designation.Designation;
import stonering.entity.unit.aspects.PlanningAspect;
import stonering.enums.TaskStatusEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.tasks.TaskContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.util.UtilByteArray;
import stonering.util.geometry.Position;
import stonering.entity.job.action.Action;
import stonering.entity.unit.Unit;
import stonering.util.global.Logger;

import java.util.LinkedList;

import static stonering.enums.unit.job.JobsEnum.NONE;

/**
 * Task object for unit behavior in the game.
 * Basically is a sequence of {@link Action}s, which consists of:
 *   preActions - performed before initial action,
 *   initialAction,
 *   postActions - performed after initial action.
 * Created with one action as initial. Actions are checked before performing, and can create additional pre- and post- actions.
 *
 * @author Alexander Kuzyakov
 */
public class Task {
    public final String name;
    public Unit performer;
    public Designation designation; // some tasks are displayed on map (e.g. digging)
    public TaskStatusEnum status; // TaskContainer uses this
    public String job; // used to filter tasks, when task is selected for unit
    public int priority; // Unit selects task with max priority (e.g. labor vs needs)


    private final Action initialAction;
    private final LinkedList<Action> preActions = new LinkedList<>();
    private final LinkedList<Action> postActions = new LinkedList<>();
    public Action nextAction; // points to first action in whole task

    public Task(String name, Action initialAction, int priority) {
        this.name = name;
        this.initialAction = initialAction;
        this.priority = priority;
        initialAction.task = this;
        status = TaskStatusEnum.OPEN;
        job = NONE.NAME;
        updateNextAction();
    }

    /**
     * Removes this task from container  if it's finished.
     */
    public void tryFinishTask() {
        if (isFinished()) GameMvc.instance().getModel().get(TaskContainer.class).removeTask(this);
    }

    /**
     * Resets this task if it was failed to return it to container.
     */
    public void reset() {
        Logger.TASKS.logDebug("Resetting task " + toString());
        preActions.clear();
        postActions.clear();
        if (performer == null) return;
        PlanningAspect planningAspect = (performer.getAspect(PlanningAspect.class));
        performer = null;
        planningAspect.interrupt();
    }

    /**
     * Task is finished, if initial action is finished, and no other action remain.
     */
    public boolean isFinished() {
        return preActions.isEmpty() && initialAction.isFinished() && postActions.isEmpty();
    }

    public void fail() {
        //TODO add interruption
        reset();
        GameMvc.instance().getModel().get(TaskContainer.class).removeTask(this);
    }

    public boolean isTaskTargetsAvailableFrom(Position position) {
        return GameMvc.instance().getModel().get(LocalMap.class).getPassage().hasPathBetween(position, initialAction.actionTarget.getPosition());
    }

    /**
     * Removes pre and post action from task
     */
    public void finishAction(Action action) {
        if (action != initialAction) {
            preActions.remove(action);
            postActions.remove(action);
        }
        updateNextAction();
    }

    public void addFirstPreAction(Action action) {
        preActions.add(0, action);
        actionAdded(action);
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
        Logger.TASKS.logDebug("Action " + action + " added to task " + name);
        action.task = this;
        updateNextAction();
    }

    public void updateNextAction() {
        if (!postActions.isEmpty()) nextAction = postActions.get(0);
        if (!initialAction.isFinished()) nextAction = initialAction;
        if (!preActions.isEmpty()) nextAction = preActions.get(0);
    }

    @Override
    public String toString() {
        return name;
    }
}