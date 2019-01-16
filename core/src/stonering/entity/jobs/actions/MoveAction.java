package stonering.entity.jobs.actions;

import stonering.entity.jobs.actions.aspects.target.PositionActionTarget;
import stonering.util.geometry.Position;

/**
 * Action for moving to tile. Has no check or logic.
 */
public class MoveAction extends Action {

    public MoveAction(Position to) {
        super(new PositionActionTarget(to, true, false));
    }

    @Override
    public boolean check() {
        return false;
    }

    @Override
    public boolean perform() {
        return false;
    }
}
