package stonering.objects.jobs.actions;

import stonering.global.utils.Position;
import stonering.objects.jobs.Task;
import stonering.objects.jobs.TaskStatusesEnum;
import stonering.objects.jobs.actions.aspects.effect.EffectAspect;
import stonering.objects.jobs.actions.aspects.requirements.RequirementsAspect;
import stonering.objects.jobs.actions.aspects.target.TargetAspect;

public class Action {
    private Task task;
    private TargetAspect targetAspect;
    private EffectAspect effectAspect;
    private RequirementsAspect requirementsAspect;
    private ActionTypeEnum actionType;
    private boolean finished;

    public Action() {
        finished = false;
    }

    public Position getTargetPosition() {
        return targetAspect.getTargetPosition();
    }

    public void setTargetPosition(Position targetPosition) {
        targetAspect.setTargetPosition(targetPosition);
    }

    public ActionTypeEnum getActionType() {
        return actionType;
    }

    public void setActionType(ActionTypeEnum actionType) {
        this.actionType = actionType;
    }

    public void perform() {
        effectAspect.perform();
    }

    public void finish() {
        finished = true;
    }

    public TargetAspect getTargetAspect() {
        return targetAspect;
    }

    public void setTargetAspect(TargetAspect targetAspect) {
        this.targetAspect = targetAspect;
    }

    public EffectAspect getEffectAspect() {
        return effectAspect;
    }

    public void setEffectAspect(EffectAspect effectAspect) {
        this.effectAspect = effectAspect;
    }

    public RequirementsAspect getRequirementsAspect() {
        return requirementsAspect;
    }

    public void setRequirementsAspect(RequirementsAspect requirementsAspect) {
        this.requirementsAspect = requirementsAspect;
    }

    public boolean isFinished() {
        return finished;
    }
}
