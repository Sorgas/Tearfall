package stonering.entity.job.action;

import stonering.entity.Entity;
import stonering.entity.building.BuildingOrder;
import stonering.entity.crafting.IngredientOrder;
import stonering.entity.item.Item;
import stonering.entity.item.selectors.ConfiguredItemSelector;
import stonering.entity.item.selectors.ItemSelector;
import stonering.entity.job.action.item.PutItemToPositionAction;
import stonering.entity.job.action.target.BuildingActionTarget;
import stonering.entity.job.designation.BuildingDesignation;
import stonering.enums.buildings.BuildingType;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.item.ItemsStream;
import stonering.util.geometry.*;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.Collection;
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
public abstract class GenericBuildingAction extends Action {
    protected final BuildingOrder order;
    private final BuildingActionTarget target;
    private ItemContainer itemContainer;

    protected GenericBuildingAction(BuildingOrder order) {
        super(new BuildingActionTarget(order));
        target = (BuildingActionTarget) actionTarget;
        this.order = order;

        takingCondition = () -> {
            return ((BuildingDesignation) task.designation).checkSite();
//            return siteValid && checkItemsAreAvailable(); // items are in the same area with performer
        };

        startCondition = () -> {
            Logger.TASKS.log("Checking " + this);
            System.out.println("checking position");
            if (!checkBuilderPosition()) return failAction(); // cannot find position for builder
            System.out.println("looking for items");
            if (!findItems()) return failAction(); // not enough items for building
            lockItems();
            System.out.println("checking items positions");
            if (checkBringingItems()) return NEW; // bring material items
            System.out.println("checking excess items");
            if (checkClearingSite()) return NEW; // remove other items
            return OK; // build
        };
    }

    private boolean checkBuilderPosition() {
        return target.builderPosition != null || target.findPositionForBuilder(order, task.performer.position);
    }

    /**
     * Looks for items in {@link ItemContainer}, and saves them to {@link IngredientOrder}s.
     * Doesn't saves items if their number is insufficient.
     * Does not locks items.
     * TODO add variants of {@link stonering.entity.item.selectors.ConfiguredItemSelector}
     *
     * @return true, if all ingredients have items.
     */
    private boolean findItems() {
        boolean ok = true;
        List<Entity> addedItems = new ArrayList<>();
        for (IngredientOrder ingredientOrder : order.parts.values()) {
            ItemSelector selector = ingredientOrder.itemSelector;
            ItemsStream itemsStream = new ItemsStream()
                    .filterNotInList(addedItems)
                    .filterBySelector(ingredientOrder.itemSelector)
                    .filterByReachability(target.builderPosition);
            List<Item> items;
            if (selector instanceof ConfiguredItemSelector) {
                items = ((ConfiguredItemSelector) selector).selectVariant(itemsStream.toList(), ingredientOrder.ingredient.quantity, target.builderPosition);
            } else {
                items = itemsStream.getNearestTo(target.getPosition(), ingredientOrder.ingredient.quantity).toList();
            }
            if (items.size() < ingredientOrder.ingredient.quantity) { // check quantity of found items
                order.parts.values().forEach(value -> value.items.clear()); // reset ingredient orders
                return false;
            }
            ingredientOrder.items.addAll(items);
            addedItems.addAll(items);
        }
        if (!ok) order.parts.values().forEach(value -> value.items.clear()); // reset ingredient orders
        return ok;
    }

    /**
     * Checks positions of items and creates actions for bringing them.
     *
     * @return true, if at least one action was created.
     */
    private boolean checkBringingItems() {
        List<Item> items =  order.parts.values().stream()
                .map(ingredientOrder -> ingredientOrder.items)
                .flatMap(Collection::stream)
                .filter(item -> !item.position.equals(target.builderPosition)) // item is far from construction site
                .collect(Collectors.toList());
        for (Item item1 : items) {
            task.addFirstPreAction(new PutItemToPositionAction(item1, target.builderPosition));
            System.out.println("put item action created " + item1.position);
        }
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
                .map(vector -> itemContainer().getItemsInPosition(vector.x, vector.y, target.center.z))
                .flatMap(Collection::stream)
                .filter(item -> !materialItems.contains(item)) // not material items
                .map(item -> new PutItemToPositionAction(item, target.builderPosition)) // create actions
                .map(task::addFirstPreAction) // add to task
                .count() > 0;
    }

    private void lockItems() {
        for (IngredientOrder value : order.parts.values()) {
            for (Item item : value.items) {
                // itemContainer() lock item
            }
        }
    }

    private void unlockItems() {

    }

    protected void consumeItems() {
        ItemContainer itemContainer = GameMvc.model().get(ItemContainer.class);
        order.parts.values().stream().map(order -> order.items).flatMap(Collection::stream).forEach(item -> {
            itemContainer.onMapItemsSystem.removeItemFromMap(item);
            itemContainer.removeItem(item);
        });
    }

    private ActionConditionStatusEnum failAction() {
        target.reset();
        unlockItems();
        clearSavedItems();
        return FAIL;
    }


    private ItemContainer itemContainer() {
        return itemContainer == null ? itemContainer = GameMvc.model().get(ItemContainer.class) : itemContainer;
    }

    private Int2dBounds getBuildingBounds() {
        Int2dBounds bounds = new Int2dBounds(target.center.x, target.center.y, target.center.x, target.center.y);
        if (!order.blueprint.construction) {
            BuildingType type = BuildingTypeMap.getBuilding(order.blueprint.building);
            IntVector2 size = RotationUtil.orientSize(type.size, order.orientation);
            bounds.extendTo(size.x - 1, size.y - 1);
        }
        return bounds;
    }

    private List<Item> getSavedMaterialItems() {
        return order.parts.values().stream()
                .map(ingredientOrder -> ingredientOrder.items)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private void clearSavedItems() {
        order.parts.values().stream()
                .map(ingredientOrder -> ingredientOrder.items)
                .forEach(List::clear);
    }
}
