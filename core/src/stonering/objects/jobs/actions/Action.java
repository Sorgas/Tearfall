package stonering.objects.jobs.actions;

import stonering.game.core.model.GameContainer;
import stonering.global.utils.Position;
import stonering.objects.jobs.Task;
import stonering.objects.jobs.actions.aspects.effect.EffectAspect;
import stonering.objects.jobs.actions.aspects.requirements.RequirementsAspect;
import stonering.objects.jobs.actions.aspects.target.TargetAspect;
import stonering.objects.local_actors.unit.Unit;

public class Action {
    private Task task;
    private ActionTypeEnum actionType;
    private Unit performer;
    private GameContainer gameContainer;

    private TargetAspect targetAspect;
    private EffectAspect effectAspect;
    private RequirementsAspect requirementsAspect;

    public Action(ActionTypeEnum actionType, GameContainer gameContainer) {
        this.actionType = actionType;
        this.gameContainer = gameContainer;
    }

    public void perform() {
        effectAspect.perform();
    }

    public void finish() {
        task.removeAction(this);
        task.recountFinished();
    }

    public Position getTargetPosition() {
        return targetAspect.getTargetPosition();
    }

    public ActionTypeEnum getActionType() {
        return actionType;
    }

    public void setActionType(ActionTypeEnum actionType) {
        this.actionType = actionType;
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

    public Unit getPerformer() {
        return performer;
    }

    public void setPerformer(Unit performer) {
        this.performer = performer;
    }

    public GameContainer getGameContainer() {
        return gameContainer;
    }

    public void setGameContainer(GameContainer gameContainer) {
        this.gameContainer = gameContainer;
    }
}
