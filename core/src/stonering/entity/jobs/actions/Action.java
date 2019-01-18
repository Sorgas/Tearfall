package stonering.entity.jobs.actions;

import stonering.entity.jobs.actions.target.ActionTarget;
import stonering.game.core.GameMvc;
import stonering.entity.jobs.Task;
import stonering.util.global.TagLoggersEnum;

/**
 * Action of a unit. Actions are parts of {@link Task}.
 * Actions do some changes in game model(digging, crafting).
 * Actions have target where unit should be to perform action.
 * Actions have requirements and create sub actions and add them to task if possible.
 * If action requirements are not met, Action and its task are failed.
 *
 * Actions should be
 */
public abstract class Action {
    protected GameMvc gameMvc;
    protected Task task; // can be modified during execution
    protected ActionTarget actionTarget;
    protected boolean finished;

    protected Action(ActionTarget actionTarget) {
        this.actionTarget = actionTarget;
        this.gameMvc = GameMvc.getInstance();
        actionTarget.setAction(this);
    }

    /**
     * Returns false if unable perform action or create sub actions.
     * Task can have more actions after this.
     */
    public abstract boolean check();

    /**
     * Applies action logic to model.
     */
    public abstract boolean perform();

    public void finish() {
        finished = true;
        task.finishAction(this);
        task.tryFinishTask();
        TagLoggersEnum.TASKS.logDebug("action " + toString() + " finished");
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public ActionTarget getActionTarget() {
        return actionTarget;
    }
}
