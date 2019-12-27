package stonering.entity.job.action.target;

import stonering.entity.job.action.MoveAction;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.entity.job.action.Action;
import stonering.util.global.Logger;

import java.util.List;
import java.util.Random;

import static stonering.entity.job.action.target.ActionTargetStatusEnum.*;

public abstract class ActionTarget {
    public ActionTargetTypeEnum targetType;

    protected Action action;
    private Random random;

    public ActionTarget(ActionTargetTypeEnum targetType) {
        this.targetType = targetType;
        random = new Random();
    }

    public abstract Position getPosition();

    /**
     * Checks if task performer has reached task target.
     * Returns fail if checked from out of map.
     */
    public ActionTargetStatusEnum check(Position performerPosition) {
        Logger.TASKS.logDebug("Checking action target " + performerPosition + getPosition());
        if(!GameMvc.instance().model().get(LocalMap.class).inMap(performerPosition)) return FAIL;
        if(!GameMvc.instance().model().get(LocalMap.class).inMap(getPosition())) return FAIL;
        int distance = getDistance(performerPosition);
        if (distance > 1) return WAIT; // target not yet reached
        switch (targetType) {
            case EXACT:
                return distance == 0 ? READY : WAIT;
            case NEAR:
                return distance == 1 ? READY : createActionToStepOff(performerPosition);
            case ANY:
                return READY; // distance is 0 or 1 here
            default: { // should never be reached
                Logger.PATH.logError("checking action target with " + targetType + " and " + performerPosition + " to " + getPosition() + " failed.");
                return FAIL;
            }
        }
    }

    public Position findPositionToStepOff(Position from) {
        Logger.TASKS.logDebug("Looking for position to step off from " + from);
        List<Position> positions = GameMvc.instance().model().get(LocalMap.class).getFreeBlockNear(from);
        if (!positions.isEmpty()) {
            return positions.get(random.nextInt(positions.size()));
        }
        Logger.PATH.logWarn("Cant find tile to step out from " + from);
        return null;
    }

    /**
     * Creates action to free target position. Can fail.
     */
    public ActionTargetStatusEnum createActionToStepOff(Position from) {
        Position to = findPositionToStepOff(from);
        if (to == null) return FAIL;
        action.task.addFirstPreAction(new MoveAction(to));
        return NEW;
    }

    //TODO check passing for neighbour
    private int getDistance(Position current) {
        Position target = getPosition();
        if (current.equals(target)) return 0;
        if (current.z == target.z && current.isNeighbour(target)) return 1;
        return 2;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
