package stonering.objects.jobs.actions.aspects.target;

import stonering.game.core.model.LocalMap;
import stonering.global.utils.Position;
import stonering.objects.jobs.actions.Action;
import stonering.objects.jobs.actions.aspects.effect.NoEffectAspect;
import stonering.objects.jobs.actions.aspects.requirements.BodyPartRequirementAspect;

public abstract class TargetAspect {
    protected Action action;
    protected boolean exactTarget;
    protected boolean nearTarget;

    public TargetAspect(Action action, boolean exactTarget, boolean nearTarget) {
        this.action = action;
        this.exactTarget = exactTarget;
        this.nearTarget = nearTarget;
    }

    public abstract Position getTargetPosition();

    public Position findPositionToStepOff() {
        LocalMap localMap = action.getGameContainer().getLocalMap();
        Position target = getTargetPosition();
        for (int x = target.getX() - 1; x < target.getX() + 2; x++) {
            for (int y = target.getY() - 1; y < target.getY() + 2; y++) {
                if (x != 0 || y != 0) {
                    if (localMap.isWalkPassable(x, y, target.getZ())) {
                        return new Position(x, y, target.getZ());
                    }
                }
            }
        }
        return null;
    }

    public boolean createActionToStepOff() {
        Position position = findPositionToStepOff();
        if (position != null) {
            Action stepOffAction = new Action(action.getGameContainer());
            stepOffAction.setEffectAspect(new NoEffectAspect(stepOffAction));
            stepOffAction.setRequirementsAspect(new BodyPartRequirementAspect(stepOffAction, "grab"));
            stepOffAction.setTargetAspect(new BlockTargetAspect(stepOffAction, position, true, false));
            return true;
        }
        return false;
    }

    public boolean isExactTarget() {
        return exactTarget;
    }

    public boolean isNearTarget() {
        return nearTarget;
    }
}
