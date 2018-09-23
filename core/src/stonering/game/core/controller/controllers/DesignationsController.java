package stonering.game.core.controller.controllers;

import stonering.enums.buildings.BuildingMap;
import stonering.enums.designations.DesignationTypes;
import stonering.game.core.GameMvc;
import stonering.game.core.model.GameContainer;
import stonering.game.core.model.lists.TaskContainer;
import stonering.game.core.view.GameView;
import stonering.global.utils.Position;
import stonering.objects.local_actors.items.Item;
import stonering.objects.local_actors.items.selectors.ItemSelector;

import java.util.ArrayList;

/**
 * Controller for various tasks. Used as builder to build tasks while user navigates through menus.
 * Digging and BuildingType combined in one controller and map,
 * because one tile can be designated for either digging or building in it.
 * Handles events from menus and updates model appropriately.
 * Has state fields representing currently chosen designation.
 * TODO Validate designations for preview sprite rendering.(move from task container)
 *
 * @author Alexander Kuzyakov on 24.12.2017.
 */
public class DesignationsController extends Controller {
    private DesignationTypes activeDesignation;
    private String building; //is set when activeDesignation is BUILD
    private boolean rectangleStarted = false;
    private Position start; // should be stored between steps
    private GameContainer container;
    private GameView view;
    private Position end;
    private ArrayList<ItemSelector> itemSelectors;
    private int priority;

    public DesignationsController(GameMvc gameMvc) {
        super(gameMvc);
        itemSelectors = new ArrayList<>();
    }

    public void init() {
        container = gameMvc.getModel();
        view = gameMvc.getView();
    }

    /**
     * Called by ui when order is finished.
     */
    public void finishTaskBuilding() {
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

    /**
     * Sets designation type to be stored between events of starting and finishing designations rectangle.
     *
     * @param activeDesignation designation type.
     * @param building          building title in {@link BuildingMap}. Is null if activeDesignation is not building.
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
        itemSelectors.clear();
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
            taskContainer.submitDesignation(position, building, itemSelectors, priority);
        } else {
            taskContainer.submitDesignation(position, activeDesignation, priority);
        }
    }

    public DesignationTypes getActiveDesignation() {
        return activeDesignation;
    }

    public void addBuildingItem(Item item) {

    }

    public void setRectangle(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    public String getBuilding() {
        return building;
    }

    public Position getStart() {
        return start;
    }

    public void addItemSelector(ItemSelector itemSelector) {
        itemSelectors.add(itemSelector);
    }

    public void clearItemSelectors() {
        itemSelectors.clear();
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
