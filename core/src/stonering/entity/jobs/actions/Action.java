package stonering.entity.jobs.actions;

import stonering.game.core.model.GameContainer;
import stonering.util.geometry.Position;
import stonering.entity.jobs.Task;
import stonering.entity.jobs.actions.aspects.effect.EffectAspect;
import stonering.entity.jobs.actions.aspects.requirements.RequirementsAspect;
import stonering.entity.jobs.actions.aspects.target.TargetAspect;
import stonering.util.global.TagLoggersEnum;

public class Action {
    protected Task task;
    protected GameContainer gameContainer;

    protected boolean finished;

    private TargetAspect targetAspect;
    private EffectAspect effectAspect;
    private RequirementsAspect requirementsAspect;

    public Action(GameContainer gameContainer) {
        this.gameContainer = gameContainer;
    }

    public boolean perform() {
        return effectAspect.perform();
    }

    public void finish() {
        finished = true;
        task.finishAction(this);
        task.tryFinishTask();
        TagLoggersEnum.TASKS.logDebug("action " + toString() + " finished");
    }

    public Position getTargetPosition() {
        return targetAspect.getTargetPosition();
    }

    public TargetAspect getTargetAspect() {
        return targetAspect;
    }

    public void setTargetAspect(TargetAspect targetAspect) {
        this.targetAspect = targetAspect;
    }

    public void setEffectAspect(EffectAspect effectAspect) {
        this.effectAspect = effectAspect;
    }

    public EffectAspect getEffectAspect() {
        return effectAspect;
    }

    public RequirementsAspect getRequirementsAspect() {
        return requirementsAspect;
    }

    public void setRequirementsAspect(RequirementsAspect requirementsAspect) {
        this.requirementsAspect = requirementsAspect;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public boolean isTargetExact() {
        return targetAspect.isExactTarget();
    }

    public boolean isTargetNear() {
        return targetAspect.isNearTarget();
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
