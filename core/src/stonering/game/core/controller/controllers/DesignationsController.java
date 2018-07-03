package stonering.game.core.controller.controllers;

import stonering.enums.designations.DesignationTypes;
import stonering.game.core.GameMvc;
import stonering.game.core.model.GameContainer;
import stonering.game.core.view.GameView;
import stonering.global.utils.Position;

/**
 * Controller for various digging and building tasks. Works with GameContainer directly.
 * Digging and BuildingType combined in one controller and map,
 * because one tile can be designated for either digging or building in it.
 * Handles events from menus and updates model appropriately.
 *
 * @author Alexander Kuzyakov on 24.12.2017.
 */
public class DesignationsController extends Controller {
    private DesignationTypes activeDesignation;
    private String building;
    private String material;
    private boolean rectangleStarted = false;
    private Position start; // should be stored between steps
    private GameContainer container;
    private GameView view;

    public DesignationsController(GameMvc gameMvc) {
        super(gameMvc);
    }

    public void init() {
        container = gameMvc.getModel();
        view = gameMvc.getView();
    }

    /**
     * Sets designation type to be stored between events of starting and finishing designations rectangle.
     *
     * @param activeDesignation designation type.
     */
    public void setActiveDesignation(DesignationTypes activeDesignation, String building) {
        this.activeDesignation = activeDesignation;
        this.building = activeDesignation == DesignationTypes.BUILD ? building : "";
        gameMvc.getView().getUiDrawer().getToolStatus().setText(
                activeDesignation != null ? activeDesignation.getText() : "" + this.building);
    }

    /**
     * Enter point.
     */
    public boolean handleDesignation() {
        if (activeDesignation != null) {
            if (rectangleStarted) {
                designate(container.getCamera().getPosition());
                start = null;
                rectangleStarted = false;
            } else {
                start = container.getCamera().getPosition().clone();
                rectangleStarted = true;
            }
            return true;
        }
        return false;
    }

    /**
     * Resets controller state.
     */
    public void handleCancel() {
        start = null;
        activeDesignation = null;
        building = "";
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

    public DesignationTypes getActiveDesignation() {
        return activeDesignation;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}
