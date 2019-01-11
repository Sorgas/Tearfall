package stonering.entity.jobs.actions;

import stonering.game.core.model.GameContainer;
import stonering.util.geometry.Position;
import stonering.entity.jobs.Task;
import stonering.util.global.TagLoggersEnum;

public abstract class Action {
    protected Task task;
    protected GameContainer gameContainer;

    protected boolean finished;

    public Action(GameContainer gameContainer) {
        this.gameContainer = gameContainer;
    }

    public final boolean perform() {
        return check() && doLogic();
    }

    protected abstract boolean doLogic();

    public abstract boolean check();

    public abstract Position getTargetPosition();

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

    public GameContainer getGameContainer() {
        return gameContainer;
    }

    public void setGameContainer(GameContainer gameContainer) {
        this.gameContainer = gameContainer;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
