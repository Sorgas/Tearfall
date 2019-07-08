package stonering.entity.job.action.target;

import stonering.entity.job.action.MoveAction;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.entity.job.action.Action;
import stonering.util.global.Logger;

import java.util.List;
import java.util.Random;

public abstract class ActionTarget {
    protected GameMvc gameMvc;
    protected Action action;
    protected boolean exactTarget; //TODO replace with enum
    protected boolean nearTarget;
    private Random random;

    public ActionTarget(boolean exactTarget, boolean nearTarget) {
        gameMvc = GameMvc.instance();
        this.exactTarget = exactTarget;
        this.nearTarget = nearTarget;
        random = new Random();
    }

    public abstract Position getPosition();

    public Position findPositionToStepOff(Position from) {
        List<Position> positions = gameMvc.getModel().get(LocalMap.class).getFreeBlockNear(from);
        if (!positions.isEmpty()) {
            return positions.get(random.nextInt(positions.size()));
        }
        Logger.PATH.logWarn("Cant find tile to step out from " + from);
        return null;
    }

    public boolean createActionToStepOff(Position from) {
        Position to = findPositionToStepOff(from);
        if (to == null) return false;
        action.getTask().addFirstPreAction(new MoveAction(to));
        return true;
    }

    /**
     * Checks if name performer has reached name target.
     */
    public boolean check(Position currentPosition) {
        if (exactTarget) {
            return currentPosition.equals(getPosition()) || nearTarget && currentPosition.isNeighbour(getPosition());
        } else {
            if (nearTarget) {
                if (currentPosition.equals(getPosition())) {
                    createActionToStepOff(currentPosition); // make 1 step away
                    return false;
                } else {
                    return currentPosition.isNeighbour(getPosition()); // near only
                }
            }
            System.out.println("WARN: name " + action + " target not defined as exact or near");
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

    public void setAction(Action action) {
        this.action = action;
    }
}
