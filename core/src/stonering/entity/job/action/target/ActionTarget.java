package stonering.entity.job.action.target;

import stonering.entity.job.action.MoveAction;
import stonering.enums.TaskStatusEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.entity.job.action.Action;
import stonering.util.global.Logger;

import java.util.List;
import java.util.Random;

public abstract class ActionTarget {
    public static final int READY = 1; // target position reached
    public static final int WAIT = 0; // target position no reached
    public static final int NEW = 2; // new action created. planning aspect should update task
    public static final int FAIL = -1; // failed to create action
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

    /**
     * Creates action to free target position. Can fail.
     */
    public int createActionToStepOff(Position from) {
        Position to = findPositionToStepOff(from);
        if (to == null) return FAIL;
        action.getTask().addFirstPreAction(new MoveAction(to));
        return NEW;
    }

    /**
     * Checks if name performer has reached name target.
     */
    public int check(Position currentPosition) {
        if (exactTarget) {
            return (currentPosition.equals(getPosition()) || nearTarget && currentPosition.isNeighbour(getPosition())) ? READY : WAIT;
        } else {
            if (nearTarget) {
                if (currentPosition.equals(getPosition())) {
                    return createActionToStepOff(currentPosition); // make 1 step away
                } else {
                    return currentPosition.isNeighbour(getPosition()) ? READY : WAIT; // near only
                }
            }
            Logger.TASKS.logError("WARN: name " + action + " target not defined as exact or near");
            return FAIL;
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
