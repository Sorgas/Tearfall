package stonering.game.core.controller.controllers;

import stonering.enums.designations.DesignationsTypes;
import stonering.game.core.model.GameContainer;
import stonering.game.core.view.GameView;
import stonering.game.core.view.ui_components.Toolbar;
import stonering.game.core.view.ui_components.menus.DiggingMenu;
import stonering.global.utils.Position;

/**
 * Created by Alexander on 24.12.2017.
 * <p>
 * controller for various digging tasks. works with GameContainer directly
 */
public class DesignationsController extends Controller {
    private DesignationsTypes activeDesignation;
    private boolean rectangleStarted = false;
    private Position start;

    public DesignationsController(GameContainer container, GameView view) {
        super(container, view);
        ((DiggingMenu) view.getUiDrawer().getToolbar().getMenu(Toolbar.DIGGING)).setController(this);
    }

    public void setActiveDesignation(DesignationsTypes activeDesignation) {
        this.activeDesignation = activeDesignation;
        view.getUiDrawer().getToolStatus().setText(
                activeDesignation != null ? activeDesignation.getText() : "");
    }

    public DesignationsTypes getActiveDesignation() {
        return activeDesignation;
    }

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

    public void handleCancel() {
        start = null;
        activeDesignation = null;
        rectangleStarted = false;
        view.getUiDrawer().getToolStatus().setText("");
    }

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
