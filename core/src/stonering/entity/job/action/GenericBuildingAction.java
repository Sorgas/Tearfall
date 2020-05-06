package stonering.entity.job.action;

import stonering.entity.building.BuildingOrder;
import stonering.entity.item.Item;
import stonering.entity.job.action.item.PutItemToPositionAction;
import stonering.entity.job.action.target.BuildingActionTarget;
import stonering.entity.job.designation.BuildingDesignation;
import stonering.enums.buildings.BuildingType;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.game.model.system.item.ItemContainer;
import stonering.util.geometry.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;

/**
 * Abstract action for creating constructions and buildings on map.
 * Building is designated with {@link BuildingDesignation} with {@link BuildingOrder}.
 * Target area should be clear from items.
 * Building can be created in the same z-level cell next to a builder,
 * or if builder can step into cell with construction after completing it (for constructing floors on SPACE cells, constructing ramps on lower level, constructing stairs).
 * <p>
 * Position for builder to stand during building is selected in the beginning of an action. This selection can fail action.
 * Materials for construction should be brought to selected builder position.
 *
 * @author Alexander on 02.10.2019.
 */
public abstract class GenericBuildingAction extends ItemConsumingAction {
    protected final BuildingOrder buildingOrder;
    private final BuildingActionTarget buildingTarget;
    private ItemContainer itemContainer;

    protected GenericBuildingAction(BuildingOrder buildingOrder) {
        super(new BuildingActionTarget(buildingOrder));
        buildingTarget = (BuildingActionTarget) target;
        this.buildingOrder = buildingOrder;

        takingCondition = () -> ((BuildingDesignation) task.designation).checkSite(); // TODO delete designation on fail

        startCondition = () -> {
            if (!checkBuilderPosition()) return failAction(); // cannot find position for builder
            // check saved items
            if(!ingredientOrdersValid()) return failAction();
            order.allIngredients.forEach(ingredientOrder -> {
                itemContainer().setItemsLocked(ingredientOrder.items, true);
            });
            if (checkBringingItems()) return NEW; // bring material items
            if (checkClearingSite()) return NEW; // remove other items
            return OK; // build
        };
    }

    private boolean checkBuilderPosition() {
        return buildingTarget.builderPosition != null || buildingTarget.findPositionForBuilder(buildingOrder, task.performer.position);
    }

    private boolean checkBringingItems() {
        List<Item> items = buildingOrder.allIngredients().stream() // all ingredients
                .map(ingredientOrder -> ingredientOrder.items)
                .flatMap(Collection::stream)
                .filter(item -> !item.position.equals(target.getPosition())) // item is far from construction site
                .collect(Collectors.toList());
        // create action for each item
        for (Item item : items) task.addFirstPreAction(new PutItemToPositionAction(item, target.getPosition()));
        return !items.isEmpty();
    }

    /**
     * Creates and adds to task actions for removing items not belonging to order from construction site.
     *
     * @return true, if at least one action has been created
     */
    private boolean checkClearingSite() {
        List<Item> materialItems = getSavedMaterialItems();
        return getBuildingBounds().stream()
                .map(vector -> itemContainer().getItemsInPosition(vector.x, vector.y, target.getPosition().z))
                .flatMap(Collection::stream)
                .filter(item -> !materialItems.contains(item)) // not material items
                .map(item -> new PutItemToPositionAction(item, target.getPosition())) // create actions
                .map(task::addFirstPreAction) // add to task
                .count() > 0;
    }

    private ActionConditionStatusEnum failAction() {
        buildingTarget.reset();
        order.allIngredients.forEach(ingredientOrder -> itemContainer().setItemsLocked(ingredientOrder.items, true));
        clearSavedItems();
        return FAIL;
    }

    protected Int2dBounds getBuildingBounds() {
        Int2dBounds bounds = new Int2dBounds(buildingOrder.position, 1, 1); // construction are 1x1
        if (!buildingOrder.blueprint.construction) {
            BuildingType type = BuildingTypeMap.getBuilding(buildingOrder.blueprint.building);
            IntVector2 size = RotationUtil.orientSize(type.size, buildingOrder.orientation);
            bounds.extendTo(size.x - 1, size.y - 1);
        }
        return bounds;
    }

    private List<Item> getSavedMaterialItems() {
        return buildingOrder.parts.values().stream()
                .map(ingredientOrder -> ingredientOrder.items)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private void clearSavedItems() {
        buildingOrder.parts.values().stream()
                .map(ingredientOrder -> ingredientOrder.items)
                .forEach(Set::clear);
    }
}
