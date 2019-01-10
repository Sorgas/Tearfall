package stonering.entity.jobs.actions.aspects.target;

import stonering.game.core.model.LocalMap;
import stonering.util.geometry.Position;
import stonering.entity.jobs.actions.Action;
import stonering.entity.jobs.actions.aspects.effect.NoEffectAspect;
import stonering.entity.jobs.actions.aspects.requirements.BodyPartRequirementAspect;

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
                    if (localMap.getPassageMap().isWalkPassable(x, y, target.getZ())) {
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
            stepOffAction.setRequirementsAspect(new BodyPartRequirementAspect(stepOffAction, "grab", true));
            stepOffAction.setTargetAspect(new BlockTargetAspect(stepOffAction, position, true, false));
            action.getTask().addFirstPreAction(stepOffAction);
            return true;
        }
        return false;
    }

    /**
     * Checks if action performer has reached action target.
     *
     * @param currentPosition
     * @return
     */
    public boolean check(Position currentPosition) {
        if (exactTarget) {
            if (nearTarget) {
                return currentPosition.getDistanse(getTargetPosition()) < 2; // exact and near
            } else {
                return currentPosition.equals(getTargetPosition()); // exact only
            }
        } else {
            if (nearTarget) {
                if (currentPosition.equals(getTargetPosition())) {
                    createActionToStepOff(); // make 1 step away
                    return false;
                } else {
                    return currentPosition.isNeighbor(getTargetPosition()); // near only
                }
            }
            System.out.println("WARN: action " + action + " target not defined as exact or near");
            return getTargetPosition().getDistanse(currentPosition) < 2; // not valid
        }
    }

    public boolean isExactTarget() {
        return exactTarget;
    }

    public boolean isNearTarget() {
        return nearTarget;
    }
}
