package stonering.game.controller.controllers.designation;

import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.EntitySelector;
import stonering.game.model.system.task.TaskContainer;
import stonering.widget.RectangleSelectComponent;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

/**
 * Designation sequence for one-step orders like digging and harvesting plants.
 * Simply allows to select area (3d box) for designation.
 *
 * @author Alexander on 21.01.2019.
 */
public class BoxDesignationSequence extends DesignationSequence {
    private DesignationTypeEnum designationType;
    private RectangleSelectComponent rectangleSelectComponent;
    private TaskContainer container;

    public BoxDesignationSequence(DesignationTypeEnum designationType) {
        this.designationType = designationType;
        container = GameMvc.instance().model().get(TaskContainer.class);
        rectangleSelectComponent = new RectangleSelectComponent(null, event -> {
            EntitySelector selector = GameMvc.instance().model().get(EntitySelector.class);
            submitSelectedFrame(selector.getFrameStart(), selector.getPosition());
            return true;
        });
    }

    /**
     * Submits designation of this sequence for all tiles in a box.
     */
    private void submitSelectedFrame(Position start, Position end) {
        Logger.TASKS.logDebug("Submitting box " + start + ", " + end + " of designation " + designationType);
        for (int x = Math.min(end.x, start.x); x <= Math.max(end.x, start.x); x++) {
            for (int y = Math.min(end.y, start.y); y <= Math.max(end.y, start.y); y++) {
                for (int z = Math.min(end.z, start.z); z <= Math.max(end.z, start.z); z++) {
                    container.designationSystem.submitDesignation(new Position(x, y, z), designationType, 1);
                }
            }
        }
    }

    @Override
    public void start() {
        Logger.TASKS.logDebug("Starting " + this.getClass().getSimpleName());
        rectangleSelectComponent.show();
    }

    @Override
    public void end() {
        rectangleSelectComponent.hide();
    }

    @Override
    public void reset() {
        end();
        start();
    }

    @Override
    public String getText() {
        return null;
    }
}
