package stonering.game.controller.controllers.designation;

import com.badlogic.gdx.scenes.scene2d.Actor;
import stonering.entity.building.Blueprint;
import stonering.entity.building.BuildingOrder;
import stonering.entity.crafting.BuildingComponent;
import stonering.enums.designations.PlaceValidatorsEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.task.TaskContainer;
import stonering.stage.toolbar.menus.Toolbar;
import stonering.util.global.Logger;
import stonering.widget.lists.ItemCardButton;
import stonering.widget.lists.MaterialSelectList;
import stonering.widget.PlaceSelectComponent;

/**
 * Designation sequence for buildings (not constructions).
 * Shows {@link PlaceSelectComponent},
 * and then material lists for each building part.
 * Creates {@link BuildingOrder} as player proceeds through widgets.
 * TODO Validation for preview sprite rendering.
 *
 * @author Alexander on 21.01.2019.
 */
public class BuildingDesignationSequence extends DesignationSequence {
    private PlaceSelectComponent placeSelectComponent;
    private BuildingOrder order; // is filed by sequence
    private Toolbar toolbar;

    public BuildingDesignationSequence(Blueprint blueprint) {
        order = new BuildingOrder(blueprint, null);
        toolbar = GameMvc.instance().view().mainUiStage.toolbar;
        createPlaceSelectComponent();
        reset();
    }

    /**
     * Creates component for selecting place for building, according to blueprint.
     * Blueprint determines position validator type for component.
     */
    private void createPlaceSelectComponent() {
        Logger.UI.logDebug("Creating placeSelectComponent for " + order.blueprint.building);
        placeSelectComponent = new PlaceSelectComponent(event -> {
            order.setPosition(placeSelectComponent.position);
            showNextList();
            return true;
        }, PlaceValidatorsEnum.getValidator(order.blueprint.placing), order.blueprint.title);
    }

    /**
     * Returns select list with items, available for given step.
     */
    private Actor createSelectListForBuildingComponent(BuildingComponent step) {
        Logger.UI.logDebug("Creating item list for step " + step.name);
        MaterialSelectList materialList = new MaterialSelectList("WS: navigate, E: select");
        materialList.fillForBuildingComponent(step, order.getPosition());
        materialList.setSelectListener(event -> { // saves selection to map and creates next list
            Logger.UI.logDebug("Selecting " + materialList.getSelectedIndex());
            if (materialList.getSelectedIndex() >= 0) {
                ItemCardButton selected = (ItemCardButton) materialList.getSelectedElement();
                //TODO handle amount requirements more than 1
                //TODO select real items instead of creating selector
                order.addItemSelectorForPart(step.name, selected.selector);
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
        for (BuildingComponent component : order.blueprint.mappedComponents.values()) {
            if (order.getItemSelectors().containsKey(component.name)) continue;  // skip already added component
            GameMvc.instance().view().mainUiStage.toolbar.addMenu(createSelectListForBuildingComponent(component));
            GameMvc.instance().getController().setCameraEnabled(false);
            return;
        }
        GameMvc.instance().model().get(TaskContainer.class).designationSystem.submitBuildingDesignation(order, 1);
        GameMvc.instance().getController().setCameraEnabled(true);
        reset();
    }

    /**
     * Resets this component to place selection. Used to designate multiple buildings.
     */
    @Override
    public void reset() {
        Logger.UI.logDebug("Resetting BuildingDesignationSequence");
        order.getItemSelectors().clear();
        placeSelectComponent.hide(); // hides all select lists
        placeSelectComponent.show();
    }

    @Override
    public void start() {
        Logger.UI.logDebug("Starting BuildingDesignationSequence");
        placeSelectComponent.show();
    }

    @Override
    public void end() {
        Logger.UI.logDebug("Ending BuildingDesignationSequence");
        placeSelectComponent.hide();
    }
}
