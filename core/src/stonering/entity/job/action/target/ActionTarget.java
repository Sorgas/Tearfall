package stonering.entity.job.action.target;

import static stonering.entity.job.action.target.ActionTargetStatusEnum.*;
import static stonering.enums.blocks.BlockTypeEnum.RAMP;

import stonering.entity.Entity;
import stonering.entity.job.action.Action;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.util.logging.Logger;

public abstract class ActionTarget {
    public ActionTargetTypeEnum type;
    public Action action;

    public ActionTarget(ActionTargetTypeEnum type) {
        this.type = type;
    }

    public abstract Position getPosition();

    /**
     * Checks if task performer has reached task target. Does not check target availability (map area).
     * Returns fail if checked from out of map.
     */
    public ActionTargetStatusEnum check(Entity performer) {
        Position performerPosition = performer.position;
        Position targetPosition = getPosition();
        int distance = getDistance(performerPosition);
        if (distance > 1) return WAIT; // target not yet reached
        switch (type) {
            case EXACT:
                return distance == 0 ? READY : WAIT;
            case NEAR:
                return distance == 1 ? READY : STEP_OFF;
            case ANY:
                return READY; // distance is 0 or 1 here
            default: { // should never be reached
                Logger.PATH.logError("checking action target with " + type + " and " + performerPosition + " to " + targetPosition + " failed.");
                throw new NullPointerException();
            }
        }
    }

    private int getDistance(Position current) {
        Position target = getPosition();
        if (current.equals(target)) return 0;
        if (!current.isNeighbour(target)) return 2;
        if (current.z == target.z) return 1;
        if (current.z < target.z && GameMvc.model().get(LocalMap.class).blockType.get(current) == RAMP.CODE) return 1;
        return 2;
    }
}
