package stonering.game.core.controller.controllers.designation;

import com.badlogic.gdx.scenes.scene2d.Actor;
import stonering.entity.local.building.BuildingType;
import stonering.entity.local.building.validators.FreeFloorValidator;
import stonering.entity.local.crafting.CommonComponentStep;
import stonering.entity.local.items.selectors.ItemSelector;
import stonering.entity.local.items.selectors.SimpleItemSelector;
import stonering.game.core.model.lists.TaskContainer;
import stonering.game.core.view.render.ui.lists.MaterialSelectList;
import stonering.game.core.view.render.ui.menus.util.PlaceSelectComponent;
import stonering.game.core.view.render.ui.util.ListItem;
import stonering.util.geometry.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Designation sequence for buildings. Shows {@link PlaceSelectComponent}, and then lists with materials for each building part.
 * TODO Validation for preview sprite rendering.
 *
 * @author Alexander on 21.01.2019.
 */
public class BuildingDesignationSequence extends DesignationSequence {
    private PlaceSelectComponent placeSelectComponent;
    private BuildingType buildingType;
    private Position position;
    private Map<CommonComponentStep, ItemSelector> stepSelectorMap;

    public BuildingDesignationSequence(BuildingType buildingType) {
        this.buildingType = buildingType;
        createPlaceSelectComponent();
        reset();
    }

    private void createPlaceSelectComponent() {
        placeSelectComponent = new PlaceSelectComponent(event -> {
            position = placeSelectComponent.getPosition();
            showNextList();
            return true;
        });
        placeSelectComponent.setPositionValidator(new FreeFloorValidator());
    }

    private void showNextList() {
        for (CommonComponentStep component : buildingType.getComponents()) {
            if (stepSelectorMap.containsKey(component)) continue;
            gameMvc.getView().getUiDrawer().getToolbar().addMenu(createSelectListForStep(component));
            return;
        }
        gameMvc.getModel().get(TaskContainer.class).submitBuildingDesignation(position, buildingType.getBuilding(), new ArrayList<>(stepSelectorMap.values()), 1);
        reset();
    }

    /**
     * Returns select list with items, available for given step.
     *
     * @param step
     * @return
     */
    private Actor createSelectListForStep(CommonComponentStep step) {
        MaterialSelectList materialList = new MaterialSelectList();
        materialList.fillForCraftingStep(step, position);
        materialList.setSelectListener(event -> { // saves selection to map and creates next list
            if (materialList.getSelectedIndex() >= 0) {
                ListItem selected = (ListItem) materialList.getSelected();
                //TODO handle amount requirements more than 1
                stepSelectorMap.put(step, new SimpleItemSelector(selected.getTitle(), selected.getMaterial(), 1));
                showNextList();
            }
            return true;
        });
        materialList.setHideListener(event -> { // cancels selection ad hides list
            stepSelectorMap.remove(step);
            materialList.hide();
            return true;
        });
        return materialList;
    }

    @Override
    public void start() {
        stepSelectorMap = new HashMap<>();
        placeSelectComponent.show();
    }

    @Override
    public void end() {
        placeSelectComponent.hide();
    }

    /**
     * Resets this component to place selection. Used for designation multiple buildings.
     */
    @Override
    public void reset() {
        stepSelectorMap.clear();
        placeSelectComponent.hide(); // hides all select lists
        placeSelectComponent.show();
    }

    @Override
    public String getText() {
        return null;
    }
}
