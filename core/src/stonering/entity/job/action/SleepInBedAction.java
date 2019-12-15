package stonering.entity.job.action;

import stonering.entity.job.action.target.ActionTarget;

/**
 * Action for sleeping.
 * TODO
 * @author Alexander on 10.09.2019.
 */
public class SleepInBedAction extends PhasedAction {

    public SleepInBedAction(ActionTarget actionTarget) {
        super(actionTarget);
    }

    @Override
    public int check() {
        return FAIL;
    }

    @Override
    protected void performLogic() {

    }
}
