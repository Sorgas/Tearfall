package stonering.game.controller.controllers.designation;

import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.model.EntitySelector;
import stonering.game.model.lists.tasks.TaskContainer;
import stonering.game.view.render.ui.menus.util.RectangleSelectComponent;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

import static stonering.enums.designations.DesignationTypeEnum.STAIRS;
import static stonering.enums.designations.DesignationTypeEnum.STAIR_FLOOR;

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

    /**
     * Creates {@link RectangleSelectComponent} which completes designation on finish.
     */
    private void createRectangleSelectComponent() {
        rectangleSelectComponent = new RectangleSelectComponent(null, event -> {
            EntitySelector selector = gameMvc.getModel().get(EntitySelector.class);
            if (designationType == STAIRS) {
                completeStairsDesignation(selector.getFrameStart(), selector.getPosition());
            } else {
                completeDesignation(selector.getFrameStart(), selector.getPosition());
            }
            return true;
        });
    }

    /**
     * Submits designation of this sequence for all tiles in a box.
     */
    private void completeDesignation(Position start, Position end) {
        TaskContainer container = gameMvc.getModel().get(TaskContainer.class);
        for (int x = Math.min(end.x, start.x); x <= Math.max(end.x, start.x); x++) {
            for (int y = Math.min(end.y, start.y); y <= Math.max(end.y, start.y); y++) {
                for (int z = Math.min(end.z, start.z); z <= Math.max(end.z, start.z); z++) {
                    container.submitOrderDesignation(new Position(x, y, z), designationType, 1); //TODO priority
                }
            }
        }
    }

    /**
     * For stairs, stairfloors are created on top z-level, stairs are created on bottom z-level.
     * Other layers are filled with combined stairs.
     */
    private void completeStairsDesignation(Position start, Position end) {
        if (start.z == end.z) return; // stairs should be at least 2 layers high.
        TaskContainer container = gameMvc.getModel().get(TaskContainer.class);
        int maxZ = Math.max(start.z, end.z);
        int minZ = Math.min(start.z, end.z);
        for (int x = Math.min(start.x, end.x); x <= Math.max(end.x, start.x); x++) {
            for (int y = Math.min(end.y, start.y); y <= Math.max(end.y, start.y); y++) {
                container.submitOrderDesignation(new Position(x, y, maxZ), STAIR_FLOOR, 1); //TODO priority
                container.submitOrderDesignation(new Position(x, y, minZ), STAIRS, 1); //TODO priority
                for (int z = minZ + 1; z < maxZ; z++) {
                    container.submitOrderDesignation(new Position(x, y, z), STAIRS, 1); //TODO priority
                }
            }
        }
    }

    @Override
    public void start() {
        Logger.TASKS.logDebug("Starting SimpleDesignationSequence for " + designationType);
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
