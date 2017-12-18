package stonering.objects.jobs.actions;

import stonering.global.utils.Position;
import stonering.objects.jobs.TaskStatusesEnum;

public class Action {
    private String target;
    private String name;
    private TaskStatusesEnum status;
    private String requirements;
    private Position targetPosition;
    private ActionTypeEnum actionType;

    public Position getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(Position targetPosition) {
        this.targetPosition = targetPosition;
    }

    public ActionTypeEnum getActionType() {
        return actionType;
    }

    public void setActionType(ActionTypeEnum actionType) {
        this.actionType = actionType;
    }
}
