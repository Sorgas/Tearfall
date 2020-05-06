package stonering.entity.job.action;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import stonering.entity.crafting.IngredientOrder;
import stonering.entity.crafting.ItemConsumingOrder;
import stonering.entity.item.Item;
import stonering.entity.item.selectors.ConfiguredItemSelector;
import stonering.entity.job.action.target.ActionTarget;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.item.ItemsStream;
import stonering.util.geometry.Position;

/**
 * Superclass for {@link CraftItemAction} and {@link BuildingAction}.
 * Provides methods for finding items for order's ingredients.
 * Removes ingredient items from container when finished.
 *
 * @author Alexander on 05.05.2020
 */
public abstract class ItemConsumingAction extends Action {
    public ItemConsumingOrder order;
    private ItemContainer itemContainer;

    protected ItemConsumingAction(ActionTarget target) {
        super(target);
    }

    /**
     * Checks items of all ingredients. If items became invalid, clears them.
     * Then, finds items for all cleared ingredients.
     * @return true, if all ingredients have valid items.
     */
    protected boolean updateItems() {
        List<IngredientOrder> invalidIngredients = order.allIngredients().stream()
                .filter(ingredientOrder -> !ingredientOrderValid(ingredientOrder))
                .collect(Collectors.toList());
        invalidIngredients.forEach(this::clearIngredientItems); // clear all invalid ingredients
        for (IngredientOrder invalidOrder : invalidIngredients) {
            if(!findItemsForIngredient(invalidOrder)) return false; // no items found for some ingredient
        }
        return true;
    }

    private boolean findItemsForIngredient(IngredientOrder ingredientOrder) {
        List<Item> otherItems = order.allIngredients().stream()
                .flatMap(ingOrder -> ingOrder.items.stream())
                .collect(Collectors.toList()); // items saved in other ingredients should not be selected
        ItemsStream validItems = new ItemsStream()
                .filterNotInList(otherItems)
                .filterBySelector(ingredientOrder.itemSelector)
                .filterByReachability(target.getPosition());
        List<Item> items;

        if (ingredientOrder.itemSelector instanceof ConfiguredItemSelector) { // select items of one type/material of allowed ones
            items = ((ConfiguredItemSelector) ingredientOrder.itemSelector).selectVariant(validItems.toList(), ingredientOrder.ingredient.quantity, getPositionForItems());
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

    private void clearIngredientItems(IngredientOrder ingredientOrder) {
        itemContainer().setItemsLocked(ingredientOrder.items, false);
        ingredientOrder.items.clear();
    }

    protected void consumeItems() {
        order.parts.values().stream().map(order -> order.items).flatMap(Collection::stream).forEach(item -> {
            itemContainer().onMapItemsSystem.removeItemFromMap(item);
            itemContainer().removeItem(item);
        });
    }

    protected ItemContainer itemContainer() {
        return itemContainer == null ? itemContainer = GameMvc.model().get(ItemContainer.class) : itemContainer;
    }

    protected abstract Position getPositionForItems();
}
