package stonering.game.controller.controllers.designation;

import com.badlogic.gdx.scenes.scene2d.Actor;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.entity.building.BuildingOrder;
import stonering.entity.crafting.IngredientOrder;
import stonering.enums.designations.PlaceValidatorsEnum;
import stonering.enums.items.recipe.Ingredient;
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
            order.position = placeSelectComponent.position;
            showNextList();
            return true;
        }, PlaceValidatorsEnum.getValidator(order.blueprint.placing), order.blueprint.title);
    }

    /**
     * Returns select list with items, available for given step.
     * On select, list adds selected item to order.
     */
    private Actor createSelectListForIngredient(String partName) {
        Logger.UI.logDebug("Creating item list for step " + partName);
        Ingredient ingredient = order.blueprint.parts.get(partName);
        MaterialSelectList materialList = new MaterialSelectList("WS: navigate, E: select");
        materialList.fillForIngredient(ingredient, order.position);
        materialList.setSelectListener(event -> { // saves selection to map and creates next list
            Logger.UI.logDebug("Selecting " + materialList.getSelectedIndex());
            if (materialList.getSelectedIndex() >= 0) {
                //TODO handle amount requirements more than 1
                //TODO select real items instead of creating selector
//                ((ItemCardButton ) materialList.getSelectedElement()).
                IngredientOrder ingredientOrder = new IngredientOrder(ingredient);
                ingredientOrder.itemSelector = ((ItemCardButton) materialList.getSelectedElement()).selector;
                order.parts.put(partName, ingredientOrder);
                Logger.UI.logDebug("Part " + partName + " added to building order.");
                showNextList();
            } else {
                Logger.UI.logWarn("Material select list handles select when no item is selected.");
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
     * Shows list of items for unfilled blueprint part. Player selects specific item for each part.
     * Submits order to {@link TaskContainer} when all parts have selected items.
     */
    private void showNextList() {
        for (String partName : order.blueprint.parts.keySet()) {
            if (order.parts.containsKey(partName)) continue;  // skip already added component
            GameMvc.instance().controller().entitySelectorInputAdapter.setEnabled(false);
            GameMvc.instance().view().mainUiStage.toolbar.addMenu(createSelectListForIngredient(partName));
            GameMvc.instance().controller().setSelectorEnabled(false);
            return;
        }
        GameMvc.instance().model().get(TaskContainer.class).designationSystem.submitBuildingDesignation(order, 1);
        GameMvc.instance().controller().setSelectorEnabled(true);
        reset();
    }

    /**
     * Resets this component to place selection. Used to designate multiple buildings.
     */
    @Override
    public void reset() {
        Logger.UI.logDebug("Resetting BuildingDesignationSequence");
        placeSelectComponent.hide(); // hides all select lists
        placeSelectComponent.show();
        GameMvc.instance().controller().entitySelectorInputAdapter.setEnabled(true);
    }

    @Override
    public void start() {
        Logger.UI.logDebug("Starting BuildingDesignationSequence");
        placeSelectComponent.show();
        GameMvc.instance().controller().entitySelectorInputAdapter.setEnabled(true);
    }

    @Override
    public void end() {
        Logger.UI.logDebug("Ending BuildingDesignationSequence");
        placeSelectComponent.hide();
        GameMvc.instance().controller().entitySelectorInputAdapter.setEnabled(true);
    }
}
