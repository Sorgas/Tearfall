package stonering.entity.job.action.target;

import stonering.entity.Entity;
import stonering.entity.job.action.MoveAction;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.game.model.local_map.passage.NeighbourPositionStream;
import stonering.util.geometry.Position;
import stonering.entity.job.action.Action;
import stonering.util.logging.Logger;

import java.util.Random;

import static stonering.entity.job.action.target.ActionTargetStatusEnum.*;

public abstract class ActionTarget {
    public ActionTargetTypeEnum type;

    protected Action action;
    private Random random;

    public ActionTarget(ActionTargetTypeEnum type) {
        this.type = type;
        random = new Random();
    }

    public abstract Position getPosition();

    /**
     * Checks if task performer has reached task target. Does not check target availability (map area).
     * Returns fail if checked from out of map.
     */
    public ActionTargetStatusEnum check(Entity performer) {
        Position performerPosition = performer.position;
//        System.out.print("P: " + performerPosition + ", T: " + getPosition());
        Position targetPosition = getPosition();
        int distance = getDistance(performerPosition);
        if (distance > 1) return WAIT; // target not yet reached
        switch (type) {
            case EXACT:
                return distance == 0 ? READY : WAIT;
            case NEAR:
                return distance == 1 ? READY : createActionToStepOff(performerPosition);
            case ANY:
                return READY; // distance is 0 or 1 here
            default: { // should never be reached
                Logger.PATH.logError("checking action target with " + type + " and " + performerPosition + " to " + targetPosition + " failed.");
                return FAIL;
            }
        }
    }

    public Position findPositionToStepOff(Position from) {
        Logger.TASKS.logDebug("Looking for position to step off from " + from);
        Position position =  new NeighbourPositionStream(from)
                .filterSameZLevel()
                .filterConnectedToCenter()
                .stream.findAny().orElse(null);
        if(position == null) Logger.PATH.logWarn("Cant find tile to step out from " + from);
        return position;
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
        if(current == null || target == null) {
            System.out.println();
        }
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
