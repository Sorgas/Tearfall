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
 * Finds items for order's ingredient, using {@link ItemConsumingAction#getPositionForItems} to check availability. 
 * Updates lists of items in ingredient orders.
 *
 * @author Alexander on 05.05.2020
 */
public abstract class ItemConsumingAction extends Action {
    public ItemConsumingOrder order;
    private ItemContainer itemContainer;
    private LocalMap map;
    
    protected ItemConsumingAction(ActionTarget target) {
        super(target);
    }

    /**
     * Checks items of all ingredients. If items became invalid, clears them.
     * Then, finds items for all cleared ingredients.
     * @return true, if all ingredients have valid items.
     */
    protected boolean ingredientOrdersValid() {
        List<IngredientOrder> invalidIngredients = order.allIngredients().stream() // collect invalid ingredients
                .filter(ingredientOrder -> !isIngredientOrderValid(ingredientOrder))
                .collect(Collectors.toList());
        invalidIngredients.forEach(this::clearIngredientItems); // clear all invalid ingredients
        for (IngredientOrder invalidOrder : invalidIngredients) {
            if(!findItemsForIngredient(invalidOrder)) return false; // search for items for ingredient
        }
        return true;
    }

    /**
     * Checks that all items are accessible and not spoiled, destroyed or locked.
     */
    private boolean isIngredientOrderValid(IngredientOrder ingredientOrder) {
        return ingredientOrder.items.stream()
                .allMatch(item -> !item.destroyed && !item.locked &&
                        itemContainer().itemAccessible(item, task.performer.position));
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
    
    protected LocalMap map() {
        return map == null ? map = GameMvc.model().get(LocalMap.class) : map;
    }
    
    protected abstract Position getPositionForItems();
}
