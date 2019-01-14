package stonering.entity.jobs.actions.aspects.target;

import stonering.entity.jobs.actions.MoveAction;
import stonering.game.core.GameMvc;
import stonering.util.geometry.Position;
import stonering.entity.jobs.actions.Action;
import stonering.util.global.TagLoggersEnum;

import java.util.List;
import java.util.Random;

public abstract class ActionTarget {
    protected GameMvc gameMvc;
    protected Action action;
    protected boolean exactTarget;
    protected boolean nearTarget;
    private Random random;

    public ActionTarget(Action action, boolean exactTarget, boolean nearTarget) {
        gameMvc = GameMvc.getInstance();
        this.action = action;
        this.exactTarget = exactTarget;
        this.nearTarget = nearTarget;
        random = new Random();
    }

    public abstract Position getPosition();

    public Position findPositionToStepOff(Position from) {
        List<Position> positions = gameMvc.getModel().getLocalMap().getFreeBlockNear(from);
        if (!positions.isEmpty()) {
            return positions.get(random.nextInt(positions.size())); //TODO add random
        }
        TagLoggersEnum.PATH.logWarn("Cant find tile to step out from " + from);
        return null;
    }

    public boolean createActionToStepOff(Position from) {
        Position to = findPositionToStepOff(from);
        if(to == null) return false;
        action.getTask().addFirstPreAction(new MoveAction(to));
        return true;
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
                return currentPosition.getDistanse(getPosition()) < 2; // exact and near
            } else {
                return currentPosition.equals(getPosition()); // exact only
            }
        } else {
            if (nearTarget) {
                if (currentPosition.equals(getPosition())) {
                    createActionToStepOff(currentPosition); // make 1 step away
                    return false;
                } else {
                    return currentPosition.isNeighbor(getPosition()); // near only
                }
            }
            System.out.println("WARN: action " + action + " target not defined as exact or near");
            return getPosition().getDistanse(currentPosition) < 2; // not valid
        }
    }

    public boolean isExactTarget() {
        return exactTarget;
    }

    public boolean isNearTarget() {
        return nearTarget;
    }

    public Action getAction() {
        return action;
    }
}
