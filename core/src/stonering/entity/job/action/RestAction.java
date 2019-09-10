package stonering.entity.job.action;

import stonering.entity.job.action.target.ActionTarget;

/**
 * Action for sleeping.
 * TODO
 * @author Alexander on 10.09.2019.
 */
public class RestAction extends Action {

    public RestAction(ActionTarget actionTarget) {
        super(actionTarget);
    }

    @Override
    public int check() {
        return 0;
    }

    @Override
    protected void performLogic() {

    }
}
