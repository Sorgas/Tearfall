package stonering.entity.job.action.target;

import stonering.entity.job.action.MoveAction;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.entity.job.action.Action;
import stonering.util.global.Logger;

import java.util.List;
import java.util.Random;

import static stonering.entity.job.action.target.ActionTargetStatusEnum.*;

public abstract class ActionTarget {
    public static final int EXACT = 0;
    public static final int NEAR = 1;
    public static final int ANY = 2;
    private int targetPlacement;

    protected Action action;
    private Random random;

    public ActionTarget(int targetPlacement) {
        this.targetPlacement = targetPlacement;
        random = new Random();
    }

    public abstract Position getPosition();

    /**
     * Checks if task performer has reached task target.
     * Returns fail if checked from out of map.
     */
    public ActionTargetStatusEnum check(Position currentPosition) {
        if(!GameMvc.instance().getModel().get(LocalMap.class).inMap(currentPosition)) return FAIL;
        int distance = getDistance(currentPosition);
        if (distance > 1) return WAIT; // target not yet reached
        switch (targetPlacement) {
            case EXACT:
                return distance == EXACT ? READY : WAIT;
            case NEAR:
                return distance == NEAR ? READY : createActionToStepOff(currentPosition);
            case ANY:
                return READY; // distance is 0 or 1 here
            default: { // should never be reached
                Logger.PATH.logError("checking action target with " + targetPlacement + " and " + currentPosition + " to " + getPosition() + " failed.");
                return FAIL;
            }
        }
    }

    public Position findPositionToStepOff(Position from) {
        List<Position> positions = GameMvc.instance().getModel().get(LocalMap.class).getFreeBlockNear(from);
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
    private int getDistance(Position currentPosition) {
        Position targetPosition = getPosition();
        if (currentPosition.equals(targetPosition)) return EXACT;
        if (currentPosition.isNeighbour(targetPosition)) return NEAR;
        return 2;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public int getTargetPlacement() {
        return targetPlacement;
    }
}
