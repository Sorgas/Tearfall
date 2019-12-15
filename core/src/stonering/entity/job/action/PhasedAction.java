package stonering.entity.job.action;

import stonering.entity.job.action.target.ActionTarget;

import java.util.ArrayList;
import java.util.List;

/**
 * Action which has some phases. Behaviour is different on different phases.
 *
 * @author Alexander on 15.12.2019.
 */
public abstract class PhasedAction extends Action {
    public List<ActionPhase> phases;

    protected PhasedAction(ActionTarget actionTarget) {
        super(actionTarget);
        phases = new ArrayList<>();
    }
}
