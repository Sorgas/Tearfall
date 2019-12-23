package stonering.entity.job.action.phase;

import stonering.entity.job.action.Action;
import stonering.entity.job.action.target.ActionTarget;
import stonering.util.global.Logger;

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
        recreatePhases();
    }

    @Override
    protected void applyWork() {
        if(phases.isEmpty()) {
            Logger.TASKS.logWarn("trying to perform phased action with no phases. " + this);
        } else {
            if(phases.get(0).perform(getWorkDelta())) phases.remove(0);
        }
    }

    @Override
    protected final void performLogic() {}

    @Override
    public boolean isFinished() {
        return super.isFinished();
    }

    /**
     * Creates phases with initial state.
     */
    protected abstract void recreatePhases();

    @Override
    public void reset() {
        super.reset();
        recreatePhases();
    }
}
