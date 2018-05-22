package stonering.game.core.controller.controllers;

import stonering.enums.designations.DesignationTypes;
import stonering.game.core.model.GameContainer;
import stonering.game.core.view.GameView;
import stonering.game.core.view.ui_components.Toolbar;
import stonering.game.core.view.ui_components.menus.DiggingMenu;
import stonering.global.utils.Position;

/**
 * Controller for various digging tasks. Works with GameContainer directly
 *
 * Created by Alexander on 24.12.2017.
 */
public class DesignationsController extends Controller {
    private DesignationTypes activeDesignation;
    private boolean rectangleStarted = false;
    private Position start; // should be stored between steps

    public DesignationsController(GameContainer container, GameView view) {
        super(container, view);
        ((DiggingMenu) view.getUiDrawer().getToolbar().getMenu(Toolbar.DIGGING)).setController(this);
    }

    /**
     * Sets designation type to be stored between events of starting and finishing designations rectangle.
     *
     * @param activeDesignation designation type.
     */
    public void setActiveDesignation(DesignationTypes activeDesignation) {
        this.activeDesignation = activeDesignation;
        view.getUiDrawer().getToolStatus().setText(
                activeDesignation != null ? activeDesignation.getText() : "");
    }

    public DesignationTypes getActiveDesignation() {
        return activeDesignation;
    }

    /**
     * Enter point.
     */
    public void handleDesignation() {
        if (activeDesignation != null) {
            if (rectangleStarted) {
                designate(container.getCamera().getPosition());
                start = null;
                rectangleStarted = false;
            } else {
                start = container.getCamera().getPosition().clone();
                rectangleStarted = true;
            }
        }
    }

    /**
     * Resets controller state.
     */
    public void handleCancel() {
        start = null;
        activeDesignation = null;
        rectangleStarted = false;
        view.getUiDrawer().getToolStatus().setText("");
    }

    /**
     * Applies designation to game through TaskContainer.
     * Also used for cancelling applied designaton.
     *
     * @param end end point of rectangle.
     */
    private void designate(Position end) {
        for (int x = Math.min(end.getX(), start.getX()); x <= Math.max(end.getX(), start.getX()); x++) {
            for (int y = Math.min(end.getY(), start.getY()); y <= Math.max(end.getY(), start.getY()); y++) {
                for (int z = Math.min(end.getZ(), start.getZ()); z <= Math.max(end.getZ(), start.getZ()); z++) {
                    container.getTaskContainer().addDesignation(new Position(x, y, z), activeDesignation);
                }
            }
        }
    }
}
