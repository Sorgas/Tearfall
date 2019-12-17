package stonering.entity.job.action.sleep_action;

import stonering.entity.job.action.PhasedAction;
import stonering.entity.job.action.target.ActionTarget;

/**
 * Action for sleeping.
 * TODO
 * @author Alexander on 10.09.2019.
 */
public class SleepInBedAction extends PhasedAction {

    public SleepInBedAction(ActionTarget actionTarget) {
        super(actionTarget);
        // create phases
    }

    @Override
    protected void recreatePhases() {
        phases.add(new RestPhase()); // 5-240 min
        //      +                 -
        // pain               tiredness

        phases.add(new SleepPhase()); // 30-720 min
        //      +                 -
        // personal treat      pain
        // illness
        // tiredness
    }

    @Override
    public int check() {
        return FAIL;
    }

    @Override
    protected void performLogic() {
        // all logic in last phase
    }

    @Override
    protected float getWorkDelta() {
        //TODO consider bed quality, personal treats, conditions
        return 0.02f;
    }

    private float getRequiredRestAmount() {

    }

    /**
     * Gets length of sleep (in work units). High tiredness, being ill
     */
    private float getRequiredSleepAmount() {

    }
}
