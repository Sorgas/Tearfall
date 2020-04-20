package stonering.entity.job.action;

import stonering.entity.building.BuildingOrder;
import stonering.entity.crafting.IngredientOrder;
import stonering.entity.item.Item;
import stonering.entity.item.selectors.ConfiguredItemSelector;
import stonering.entity.job.action.item.PutItemToPositionAction;
import stonering.entity.job.action.target.BuildingActionTarget;
import stonering.entity.job.designation.BuildingDesignation;
import stonering.enums.buildings.BuildingType;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.item.ItemsStream;
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
public abstract class GenericBuildingAction extends Action {
    protected final BuildingOrder order;
    private final BuildingActionTarget buildingTarget;
    private ItemContainer itemContainer;

    protected GenericBuildingAction(BuildingOrder order) {
        super(new BuildingActionTarget(order));
        buildingTarget = (BuildingActionTarget) target;
        this.order = order;

        takingCondition = () -> ((BuildingDesignation) task.designation).checkSite(); // TODO delete designation on fail

        startCondition = () -> {
            if (!checkBuilderPosition()) return failAction(); // cannot find position for builder

            // check saved items
            if(!updateItems()) return failAction();
            lockItems();
            if (checkBringingItems()) return NEW; // bring material items
            if (checkClearingSite()) return NEW; // remove other items
            return OK; // build
        };
    }

    private boolean checkBuilderPosition() {
        return buildingTarget.builderPosition != null || buildingTarget.findPositionForBuilder(order, task.performer.position);
    }

    /**
     * Checks items of all ingredients. If items became invalid, clears them.
     * Then, finds items for all cleared ingredients.
     */
    private boolean updateItems() {
        List<IngredientOrder> invalidIngredients = order.parts.values().stream()
                .filter(ingredientOrder -> !ingredientOrderValid(ingredientOrder))
                .collect(Collectors.toList());
        invalidIngredients.forEach(this::clearIngredientItems); // clear all invalid ingredients
        for (IngredientOrder invalidOrder : invalidIngredients) {
            if(!findItemsForIngredient(invalidOrder)) return false; // no items found for some ingredient
        }
        return true;
    }

    private void clearIngredientItems(IngredientOrder ingredientOrder) {
        ingredientOrder.items.clear();
        // unlock items
    }

    private boolean findItemsForIngredient(IngredientOrder ingredientOrder) {
        List<Item> otherItems = order.parts.values().stream()
                .flatMap(ingredientOrder1 -> ingredientOrder1.items.stream())
                .collect(Collectors.toList()); // saved items in other ingredients should not be selected
        ItemsStream validItems = new ItemsStream()
                .filterNotInList(otherItems)
                .filterBySelector(ingredientOrder.itemSelector)
                .filterByReachability(target.getPosition());
        List<Item> items;
        if (ingredientOrder.itemSelector instanceof ConfiguredItemSelector) { // select items of one type/material of allowed ones
            items = ((ConfiguredItemSelector) ingredientOrder.itemSelector)
                    .selectVariant(validItems.toList(), ingredientOrder.ingredient.quantity, buildingTarget.builderPosition);
        } else { // select nearest of unique items
            items = validItems.getNearestTo(target.getPosition(), ingredientOrder.ingredient.quantity).toList();
        }
        if (items.size() < ingredientOrder.ingredient.quantity) return false; // not enough items found
        ingredientOrder.items.addAll(items); // save found items to order
        return true;
    }

    private boolean ingredientOrderValid(IngredientOrder ingredientOrder) {
        LocalMap map = GameMvc.model().get(LocalMap.class);
        byte performerArea = map.passageMap.area.get(task.performer.position);
        return ingredientOrder.items.stream()
                .filter(item -> ingredientOrder.itemSelector.checkItem(item)) // item is still ok
                .map(item -> item.position)
                .map(map.passageMap.area::get)
                .filter(area -> area == performerArea) // item is still reachable
                .count() == ingredientOrder.ingredient.quantity;
    }

    private boolean checkBringingItems() {
        List<Item> items = order.parts.values().stream() // all ingredients
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
        order.parts.values().stream().map(order -> order.items).flatMap(Collection::stream).forEach(item -> {
            itemContainer().onMapItemsSystem.removeItemFromMap(item);
            itemContainer().removeItem(item);
        });
    }

    private ActionConditionStatusEnum failAction() {
        buildingTarget.reset();
        unlockItems();
        clearSavedItems();
        return FAIL;
    }

    protected Int2dBounds getBuildingBounds() {
        Int2dBounds bounds = new Int2dBounds(order.position, 1, 1); // construction are 1x1
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
                .forEach(Set::clear);
    }

    private ItemContainer itemContainer() {
        return itemContainer == null ? itemContainer = GameMvc.model().get(ItemContainer.class) : itemContainer;
    }
}
