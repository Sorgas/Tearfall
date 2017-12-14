package stonering.objects.jobs;

import stonering.global.utils.Position;

public class Action {
    private String name;
    private TaskStatusesEnum status;
    private String action;
    private String target;
    private String requirements;
    private Position targetPosition;

    public Position getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(Position targetPosition) {
        this.targetPosition = targetPosition;
    }
}
