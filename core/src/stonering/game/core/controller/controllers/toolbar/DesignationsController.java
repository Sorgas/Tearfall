package stonering.game.core.controller.controllers.toolbar;

import com.badlogic.gdx.scenes.scene2d.Actor;
import stonering.entity.local.building.BuildingType;
import stonering.entity.local.building.validators.FreeFloorValidator;
import stonering.entity.local.crafting.CommonComponentStep;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.core.GameMvc;
import stonering.game.core.controller.controllers.Controller;
import stonering.game.core.model.GameContainer;
import stonering.game.core.view.GameView;
import stonering.game.core.view.render.ui.lists.MaterialSelectList;
import stonering.game.core.view.render.ui.menus.util.AreaSelectComponent;
import stonering.game.core.view.render.ui.menus.util.PlaceSelectComponent;
import stonering.global.utils.Position;
import stonering.entity.local.items.selectors.ItemSelector;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for designating various tasks. Used as builder to build tasks while user navigates through menus.
 * Digging and BuildingType combined in one controller and map,
 * because one tile can be designated for either digging or buildingType in it.
 * Handles events from menus and updates model appropriately.
 * Has state fields representing currently chosen designation.
 * <p>
 * TODO Validate designations for preview sprite rendering.(move from task container)
 *
 * @author Alexander Kuzyakov on 24.12.2017.
 */
public class DesignationsController extends Controller {
    private GameContainer container;
    private GameView view;

    private DesignationTypeEnum activeDesignation;
    private BuildingType buildingType; //is set when activeDesignation is BUILD
    private List<ItemSelector> itemSelectors; // created for each crafting step

    private PlaceSelectComponent placeSelectComponent;
    private AreaSelectComponent areaSelectComponent;
    private Position start; // should be stored between steps
    private Position end;

    private int priority;

    public DesignationsController(GameMvc gameMvc) {
        super(gameMvc);
        itemSelectors = new ArrayList<>();
        placeSelectComponent = new PlaceSelectComponent(gameMvc);
    }

    public void init() {
        container = gameMvc.getModel();
        view = gameMvc.getView();
        placeSelectComponent.init();
    }

    /**
     * Saves chosen designation type to be stored between events of starting and finishing designations rectangle and items selection.
     *
     * @param activeDesignation designation type.
     * @param building          buildingType title in {@link BuildingTypeMap}. Is null if activeDesignation is not buildingType.
     */
    public void setActiveDesignation(DesignationTypeEnum activeDesignation, BuildingType building) {
        this.activeDesignation = activeDesignation;
        this.buildingType = activeDesignation == DesignationTypeEnum.BUILD ? building : null;
        gameMvc.getView().getUiDrawer().setToolbarLabelText(activeDesignation.getText() + (building != null ? building.getTitle() : ""));
        addNextActorToToolbar();
    }

    /**
     * Showing ui components is controlled by this controller (except menu buttons which show submenus).
     * Called by leaf menu buttons and components like {@link MaterialSelectList} to proceed on creating task.
     *
     * //TODO make some unification
     */
    private void addNextActorToToolbar() {
        if (activeDesignation == DesignationTypeEnum.BUILD) {
            if (start == null) {// place not selected
                if(buildingType.getCategory().equals("constructions")) {
                    //TODO area select
                } else {
                    placeSelectComponent.setDefaultText("Place " + buildingType.getTitle());
                    placeSelectComponent.setWarningText("Cannot build " + buildingType.getTitle() + " here");
                    placeSelectComponent.setPositionValidator(new FreeFloorValidator()); // buildings should be on free floors
                    placeSelectComponent.show();
                }
            } else if (buildingType.getComponents().size() > itemSelectors.size()) { // steps not finished. called several times
                view.getUiDrawer().getToolbar().addMenu(createSelectListForStep(buildingType.getComponents().get(itemSelectors.size())));
            } else { //finish
                finishTaskBuilding();
                view.getUiDrawer().getToolbar().closeNonMenuActors();
            }
        } else {
            if (start == null) {// place not selected
                areaSelectComponent.setText(activeDesignation.getText());
            } else { //finish
                // for designations, hiding place selection is performed manually
                finishTaskBuilding();
            }
        }
    }

    /**
     * Returns select list with items, available for given step.
     *
     * @param step
     * @return
     */
    private Actor createSelectListForStep(CommonComponentStep step) {
        MaterialSelectList actor = new MaterialSelectList(gameMvc);
        actor.init();
        actor.fillForCraftingStep(step);
        return actor;
    }

    /**
     * Finishes building task.
     */
    private void finishTaskBuilding() {
        if (activeDesignation == DesignationTypeEnum.BUILD) {
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
     * Resets controller state.
     */
    public void handleCancel() {
        start = null;
        activeDesignation = null;
        buildingType = null;
        itemSelectors.clear();
        view.getUiDrawer().setToolbarLabelText("");
    }

    /**
     * Adds single point designation to task container.
     * Called several times for area orders (digging, harvesting, etc).
     *
     * @param position
     */
    private void addDesignationToContainer(Position position) {
        if (activeDesignation == DesignationTypeEnum.BUILD) {
            container.getTaskContainer().submitBuildingDesignation(position, buildingType.getBuilding(), itemSelectors, priority);
        } else {
            container.getTaskContainer().submitOrderDesignation(position, activeDesignation, priority);
        }
    }

    /**
     * Called from {@link PlaceSelectComponent} to store selected area.
     *
     * @param start
     * @param end
     */
    public void setRectangle(Position start, Position end) {
        this.start = start;
        this.end = end;
        addNextActorToToolbar();
    }

    /**
     * Adds item selector, corresponding to selected items to order.
     * Selector will be used on task checking & performing.
     *
     * @param itemSelector
     */
    public void addItemSelector(ItemSelector itemSelector) {
        itemSelectors.add(itemSelector);
        addNextActorToToolbar();
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public Position getStart() {
        return start;
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

    public DesignationTypeEnum getActiveDesignation() {
        return activeDesignation;
    }
}
