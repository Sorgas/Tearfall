package stonering.game.controller.controllers.designation;

import com.badlogic.gdx.scenes.scene2d.Actor;
import stonering.entity.building.Blueprint;
import stonering.entity.building.BuildingOrder;
import stonering.entity.crafting.BuildingComponent;
import stonering.enums.designations.PlaceValidatorsEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.tasks.TaskContainer;
import stonering.util.global.Logger;
import stonering.widget.lists.ItemCardButton;
import stonering.widget.lists.MaterialSelectList;
import stonering.widget.PlaceSelectComponent;

/**
 * Designation sequence for buildings.
 * Shows {@link PlaceSelectComponent},
 * and then material lists for each building part.
 * Creates {@link BuildingOrder} as player proceeds through widgets.
 * TODO Validation for preview sprite rendering.
 *
 * @author Alexander on 21.01.2019.
 */
public class BuildingDesignationSequence extends DesignationSequence {
    private BuildingOrder order;
    private PlaceSelectComponent placeSelectComponent;

    public BuildingDesignationSequence(Blueprint blueprint) {
        order = new BuildingOrder(blueprint, null);
        createPlaceSelectComponent();
        reset();
    }

    /**
     * Creates component for selecting place for building, according to blueprint.
     * Blueprint determines position validator type for component.
     */
    private void createPlaceSelectComponent() {
        Logger.UI.logDebug("Creating placeSelectComponent for " + order.getBlueprint().building);
        placeSelectComponent = new PlaceSelectComponent(event -> {
            order.setPosition(placeSelectComponent.getPosition());
            showNextList();
            return true;
        });
        placeSelectComponent.setPositionValidator(PlaceValidatorsEnum.getValidator(order.getBlueprint().placing));
        placeSelectComponent.defaultText = "Select place for " + order.getBlueprint().building;
        placeSelectComponent.warningText = "Wrong place for " + order.getBlueprint().building;
    }

    /**
     * Returns select list with items, available for given step.
     */
    private Actor createSelectListForStep(BuildingComponent step) {
        Logger.UI.logDebug("Creating item list for step " + step.name);
        MaterialSelectList materialList = new MaterialSelectList();
        materialList.fillForCraftingStep(step, order.getPosition());
        materialList.setSelectListener(event -> { // saves selection to map and creates next list
            Logger.UI.logDebug("Selecting " + materialList.getSelectedIndex());
            if (materialList.active && materialList.getSelectedIndex() >= 0) {
                ItemCardButton selected = (ItemCardButton) materialList.getSelectedElement();
                //TODO handle amount requirements more than 1
                order.addItemSelectorForPart(step.name, selected.getSelector());
                showNextList();
            }
            return true;
        });
        materialList.setCancelListener(event -> { // cancels selection ad hides list
            materialList.hide();
            return true;
        });
        return materialList;
    }

    /**
     * Shows list for unfilled step, or, if all steps completed, submits {@link BuildingOrder} to {@link TaskContainer}
     */
    private void showNextList() {
        for (BuildingComponent component : order.getBlueprint().mappedComponents.values()) {
            if(order.getItemSelectors().containsKey(component.name)) continue;  // skip already added component
            GameMvc.instance().getView().mainUiStage.toolbar.addMenu(createSelectListForStep(component));
            return;
        }
        GameMvc.instance().getModel().get(TaskContainer.class).submitBuildingDesignation(order, 1);
        reset();
    }

    /**
     * Resets this component to place selection. Used for designation multiple buildings.
     */
    @Override
    public void reset() {
        order.getItemSelectors().clear();
        placeSelectComponent.hide(); // hides all select lists
        placeSelectComponent.show();
    }

    @Override
    public void start() {
        placeSelectComponent.show();
    }

    @Override
    public void end() {
        placeSelectComponent.hide();
    }

    @Override
    public String getText() {
        return "Building " + order.getBlueprint().building;
    }
}
