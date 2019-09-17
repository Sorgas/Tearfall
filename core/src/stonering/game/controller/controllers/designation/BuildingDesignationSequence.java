package stonering.game.controller.controllers.designation;

import com.badlogic.gdx.scenes.scene2d.Actor;
import stonering.entity.building.Blueprint;
import stonering.entity.building.BuildingOrder;
import stonering.entity.crafting.CommonComponent;
import stonering.entity.item.selectors.SimpleItemSelector;
import stonering.enums.designations.PlaceValidatorsEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.tasks.TaskContainer;
import stonering.widget.lists.MaterialSelectList;
import stonering.widget.PlaceSelectComponent;
import stonering.widget.lists.ListItem;

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
        placeSelectComponent = new PlaceSelectComponent(event -> {
            order.setPosition(placeSelectComponent.getPosition());
            showNextList();
            return true;
        });
        placeSelectComponent.setPositionValidator(PlaceValidatorsEnum.getValidator(order.getBlueprint().getPlacing()));
    }

    /**
     * Returns select list with item, available for given step.
     */
    private Actor createSelectListForStep(CommonComponent step) {
        MaterialSelectList materialList = new MaterialSelectList();
        materialList.fillForCraftingStep(step, order.getPosition());
        materialList.setSelectListener(event -> { // saves selection to map and creates next list
            if (materialList.getSelectedIndex() >= 0) {
                ListItem selected = (ListItem) materialList.getSelected();
                //TODO handle amount requirements more than 1
                order.addItemSelectorForPart(step.getName(), new SimpleItemSelector(selected.getTitle(), selected.getMaterial(), 1));
                showNextList();
            }
            return true;
        });
        materialList.setHideListener(event -> { // cancels selection ad hides list
            materialList.hide();
            return true;
        });
        return materialList;
    }

    /**
     * Shows list for unfilled step, or, if all steps completed, submits {@link BuildingOrder} to {@link TaskContainer}
     */
    private void showNextList() {
        for (CommonComponent component : order.getBlueprint().getComponents()) {
            if(order.getItemSelectors().containsKey(component.getName())) continue;  // skip already added component
            GameMvc.instance().getView().getUiDrawer().getToolbar().addMenu(createSelectListForStep(component));
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
        return null;
    }
}
