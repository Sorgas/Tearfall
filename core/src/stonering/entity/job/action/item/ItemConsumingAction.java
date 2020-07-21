package stonering.entity.job.action.item;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import stonering.entity.crafting.IngredientOrder;
import stonering.entity.crafting.ItemConsumingOrder;
import stonering.entity.item.Item;
import stonering.entity.item.selectors.ConfiguredItemSelector;
import stonering.entity.job.action.BuildingAction;
import stonering.entity.job.action.ItemAction;
import stonering.entity.job.action.target.ActionTarget;
import stonering.enums.materials.MaterialMap;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.item.ContainedItemsStream;
import stonering.game.model.system.item.OnMapItemsStream;
import stonering.util.geometry.Position;

/**
 * Superclass for {@link CraftItemAction} and {@link BuildingAction}.
 * Provides methods for searching for and locking ingredient items.
 * During items selection uses {@link ItemConsumingAction#getPositionForItems} to check availability.
 *
 * @author Alexander on 05.05.2020
 */
public abstract class ItemConsumingAction extends ItemAction {
    public ItemConsumingOrder order;
    private LocalMap map;

    protected ItemConsumingAction(ItemConsumingOrder order, ActionTarget target) {
        super(target);
        this.order = order;
    }

    /**
     * Checks items of all ingredients. If items became invalid, clears them.
     * Then, finds items for all cleared ingredients.
     *
     * @return true, if all ingredients have valid items.
     */
    protected boolean ingredientOrdersValid() {
        Collection<IngredientOrder> orders = order.allIngredients();
        if(orders.isEmpty()) return false;
        List<IngredientOrder> invalidIngredients = orders.stream() // collect invalid ingredients
                .filter(ingredientOrder -> !isIngredientOrderValid(ingredientOrder))
                .collect(Collectors.toList());
        invalidIngredients.forEach(this::clearIngredientItems); // clear all invalid ingredients
        for (IngredientOrder invalidOrder : invalidIngredients) {
            if (!findItemsForIngredient(invalidOrder)) { // search for items for ingredient
                return false; 
            }
        }
        return true;
    }

    /**
     * Checks that ingredient has correct quantity of items and all items are accessible and not spoiled, destroyed or locked.
     */
    private boolean isIngredientOrderValid(IngredientOrder ingredientOrder) {
        return ingredientOrder.items.size() == ingredientOrder.ingredient.quantity
                && ingredientOrder.items.stream()
                .allMatch(item -> !item.destroyed &&
                        itemContainer.itemAccessible(item, task.performer.position));
    }

    private boolean findItemsForIngredient(IngredientOrder ingredientOrder) {
        List<Item> otherItems = order.allIngredients().stream()
                .flatMap(ingOrder -> ingOrder.items.stream())
                .collect(Collectors.toList()); // items saved in other ingredients should not be selected
        List<Item> validItems = new OnMapItemsStream() // items from map
                .filterNotInList(otherItems)
                .filterBySelector(ingredientOrder.itemSelector)
                .filterByReachability(target)
                .stream.filter(item -> !item.locked)
                .collect(Collectors.toList());
        validItems.addAll(new ContainedItemsStream() // items from containers
                .filterNotInList(otherItems)
                .filterBySelector(ingredientOrder.itemSelector)
                .filterByReachability(target)
                .filterLockedContainers()
                .filterOwnedContainers()
                .stream.filter(entry -> !entry.getKey().locked)
                .map(Map.Entry::getKey) 
                .collect(Collectors.toList()));
        // select items of one type/material of allowed ones
        if (ingredientOrder.itemSelector instanceof ConfiguredItemSelector)
            validItems = ((ConfiguredItemSelector) ingredientOrder.itemSelector).selectVariant(validItems, ingredientOrder.ingredient.quantity, getPositionForItems());

        // select nearest items
        validItems = new OnMapItemsStream(validItems).getNearestTo(target.getPosition(), ingredientOrder.ingredient.quantity).toList();

        if (validItems.size() < ingredientOrder.ingredient.quantity) return false; // not enough items found
        ingredientOrder.items.addAll(validItems); // save found items to order
        return true;
    }

    protected void consumeItems() {
        order.ingredientOrders.values().stream()
                .filter(order -> !"main".equals(order.ingredient.key))
                .flatMap(order -> order.items.stream()) // all items from not 'main' ingredient
                .forEach(item -> {
                    itemContainer.onMapItemsSystem.removeItemFromMap(item);
                    itemContainer.removeItem(item);
                });
    }

    protected LocalMap map() {
        return map == null ? map = GameMvc.model().get(LocalMap.class) : map;
    }

    protected void clearOrderItems() {
        order.allIngredients().forEach(this::clearIngredientItems);
    }

    protected void clearIngredientItems(IngredientOrder ingredientOrder) {
        setItemsLocked(ingredientOrder, false);
        ingredientOrder.items.clear();
    }

    protected void setItemsLocked(IngredientOrder ingredientOrder, boolean value) {
        ingredientOrder.items.forEach(item -> item.locked = value);
    }

    protected void lockItems() {
        order.allIngredients().stream()
                .flatMap(ingredientOrder -> ingredientOrder.items.stream())
                .forEach(item -> item.locked = true);
    }

    protected List<Item> getIngredientItems() {
        return order.allIngredients().stream()
                .flatMap(ingredientOrder -> ingredientOrder.items.stream())
                .collect(Collectors.toList());
    }

    /**
     * Some high-tier materials should increase time for building or crafting when used.
     * Modifiers of each used item materials are stacked and applied to recipe work amount with formula:
     * (1 + modifiers) * base
     */
    protected float getMaterialWorkAmountMultiplier() {
        return order.allIngredients().stream()
                .flatMap(ingredientOrder -> ingredientOrder.items.stream())
                .map(item -> item.material)
                .map(MaterialMap::getMaterial)
                .map(material -> material.workAmountModifier)
                .reduce(Float::sum).orElse(0f);
    }

    protected abstract Position getPositionForItems();
}
