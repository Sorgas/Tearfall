package stonering.entity.job.action;

import static stonering.entity.job.action.ActionConditionStatusEnum.FAIL;
import static stonering.entity.job.action.ActionConditionStatusEnum.OK;

import stonering.entity.job.action.target.PositionActionTarget;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.util.logging.Logger;

/**
 * Action for moving to tile. Has no check or logic.
 */
public class MoveAction extends Action {

    public MoveAction(Position to) {
        super(new PositionActionTarget(to, ActionTargetTypeEnum.EXACT));
        startCondition = () -> {
            // task.performer.get(MovementAspect.class) check can move
            return GameMvc.model().get(LocalMap.class).passageMap.inSameArea(task.performer.position, to)
                    ? OK
                    : Logger.TASKS.logWarn("Creature cannot move from " + task.performer.position + " to " + to, FAIL);
        };
    }

    @Override
    public String toString() {
        return "Move name";
    }
}
