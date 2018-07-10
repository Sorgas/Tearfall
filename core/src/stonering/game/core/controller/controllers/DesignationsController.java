package stonering.game.core.controller.controllers;

import stonering.enums.buildings.BuildingMap;
import stonering.enums.designations.DesignationTypes;
import stonering.game.core.GameMvc;
import stonering.game.core.model.GameContainer;
import stonering.game.core.model.lists.TaskContainer;
import stonering.game.core.view.GameView;
import stonering.global.utils.Position;
import stonering.objects.local_actors.items.Item;

import java.util.ArrayList;

/**
 * Controller for various digging and building tasks. Works with GameContainer directly.
 * Digging and BuildingType combined in one controller and map,
 * because one tile can be designated for either digging or building in it.
 * Handles events from menus and updates model appropriately.
 * Has state fields representing currently chosen designation.
 * TODO Validates designations for preview sprite rendering.(move from task container)
 *
 * @author Alexander Kuzyakov on 24.12.2017.
 */
public class DesignationsController extends Controller {
    private DesignationTypes activeDesignation;
    private String building; //is set when activeDesignation is BUILD
    private ArrayList<Item> items; // is set when activeDesignation is BUILD
    private boolean rectangleStarted = false;
    private Position start; // should be stored between steps
    private GameContainer container;
    private GameView view;

    public DesignationsController(GameMvc gameMvc) {
        super(gameMvc);
        items = new ArrayList<>();
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
        items.clear();
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
        if (activeDesignation == DesignationTypes.BUILD && !BuildingMap.getInstance().getBuilding(building).getCategory().equals("constructions")) {
            addDesignationToContainer(end);
        } else {
            for (int x = Math.min(end.getX(), start.getX()); x <= Math.max(end.getX(), start.getX()); x++) {
                for (int y = Math.min(end.getY(), start.getY()); y <= Math.max(end.getY(), start.getY()); y++) {
                    for (int z = Math.min(end.getZ(), start.getZ()); z <= Math.max(end.getZ(), start.getZ()); z++) {
                        addDesignationToContainer(new Position(x, y, z));
                    }
                }
            }
        }
    }

    private void addDesignationToContainer(Position position) {
        TaskContainer taskContainer = container.getTaskContainer();
        if (activeDesignation == DesignationTypes.BUILD) {
            taskContainer.submitDesignation(position, building, items);
        } else {
            taskContainer.submitDesignation(position, activeDesignation);
        }
    }

    public DesignationTypes getActiveDesignation() {
        return activeDesignation;
    }

    public void addBuildingItem(Item item) {

    }
}
