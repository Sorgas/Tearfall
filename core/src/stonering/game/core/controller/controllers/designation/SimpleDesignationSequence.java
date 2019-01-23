package stonering.game.core.controller.controllers.designation;

import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.core.model.EntitySelector;
import stonering.game.core.model.lists.TaskContainer;
import stonering.game.core.view.render.ui.menus.util.RectangleSelectComponent;
import stonering.util.geometry.Position;
import stonering.util.global.TagLoggersEnum;

/**
 * Designation sequence for orders like digging and harvesting plants.
 * Simply allows to select area for designation.
 *
 * @author Alexander on 21.01.2019.
 */
public class SimpleDesignationSequence extends DesignationSequence {
    private RectangleSelectComponent rectangleSelectComponent;
    private DesignationTypeEnum designationType;

    public SimpleDesignationSequence(DesignationTypeEnum designationType) {
        super();
        this.designationType = designationType;
        createRectangleSelectComponent();
    }

    private void createRectangleSelectComponent() {
        rectangleSelectComponent = new RectangleSelectComponent(event -> {
            EntitySelector selector = gameMvc.getModel().getCamera();
            completeDesignation(selector.getFrameStart(), selector.getPosition());
            return true;
        });
    }

    private void completeDesignation(Position start, Position end) {
        TaskContainer container = gameMvc.getModel().getTaskContainer();
        for (int x = Math.min(end.getX(), start.getX()); x <= Math.max(end.getX(), start.getX()); x++) {
            for (int y = Math.min(end.getY(), start.getY()); y <= Math.max(end.getY(), start.getY()); y++) {
                for (int z = Math.min(end.getZ(), start.getZ()); z <= Math.max(end.getZ(), start.getZ()); z++) {
                    container.submitOrderDesignation(new Position(x, y, z), designationType, 1); //TODO priority
                }
            }
        }
    }

    @Override
    public void start() {
        TagLoggersEnum.TASKS.logDebug("Starting SimpleDesignationSequence for " + designationType);
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
