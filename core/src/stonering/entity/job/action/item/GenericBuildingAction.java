package stonering.entity.job.action.item;

import stonering.entity.building.BuildingOrder;
import stonering.entity.item.Item;
import stonering.entity.job.action.ActionConditionStatusEnum;
import stonering.entity.job.action.equipment.use.PutItemToPositionAction;
import stonering.entity.job.action.target.BuildingActionTarget;
import stonering.entity.job.designation.BuildingDesignation;
import stonering.enums.buildings.BuildingType;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.util.geometry.*;

import java.util.List;
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

    protected GenericBuildingAction(BuildingOrder buildingOrder) {
        super(buildingOrder, new BuildingActionTarget(buildingOrder));
        buildingTarget = (BuildingActionTarget) target;
        this.buildingOrder = buildingOrder;
        
        startCondition = () -> {
            if (!checkBuilderPosition()) return failAction(); // find position for builder
            if(order.ingredientOrders.containsKey("main")) return failAction(); // buildings cannot have main ingredient
            if(!ingredientOrdersValid()) {
                // suspend designation
                return failAction(); // check/find items for order
            }
            lockItems(); // lock valid items
            if (checkBringingItems()) return NEW; // bring material items
            if (checkClearingSite()) return NEW; // remove other items
//            if(buildingTarget.builderPosition.equals(task.performer.position))
//                addPreAction(new )
            System.out.println("build action ok");
            return OK; // build
        };
    }

    private boolean checkBuilderPosition() {
        return buildingTarget.builderPosition != null || buildingTarget.findPositionForBuilder(buildingOrder, task.performer.position);
    }

    private boolean checkBringingItems() {
        List<Item> items = getIngredientItems().stream()
                .filter(item -> !item.position.equals(target.getPosition())) // item is not on construction site
                .collect(Collectors.toList());
        // create action for each item
        items.forEach(item -> task.addFirstPreAction(new PutItemToPositionAction(item, target.getPosition())));
        return !items.isEmpty();
    }

    /**
     * Creates and adds to task actions for removing items not belonging to order from construction site.
     *
     * @return true, if at least one action has been created
     */
    private boolean checkClearingSite() {
        List<Item> materialItems = getIngredientItems();
        List<Item> excessItems = getBuildingBounds().stream()
                .flatMap(vector -> itemContainer.getItemsInPosition(vector.x, vector.y, target.getPosition().z).stream())
                .filter(item -> !materialItems.contains(item)) // not material items
                .collect(Collectors.toList());
        excessItems.stream()
                .map(item -> new PutItemToPositionAction(item, target.getPosition())) // create actions
                .forEach(task::addFirstPreAction); // add to task
        return !excessItems.isEmpty();
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

    private ActionConditionStatusEnum failAction() {
        buildingTarget.reset();
        clearOrderItems();
        return FAIL;
    }
    
    protected Position getPositionForItems() {
        return buildingTarget.getPosition(); 
    }
}
