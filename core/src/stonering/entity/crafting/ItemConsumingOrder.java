package stonering.entity.crafting;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Superclass for {@link ItemOrder} and {@link stonering.entity.building.BuildingOrder}.
 * Stores map of {@link IngredientOrder}s.
 *
 * @author Alexander on 05.05.2020
 */
public class ItemConsumingOrder {
    public final Map<String, IngredientOrder> ingredientOrders;
    private Collection<IngredientOrder> allIngredients;

    public ItemConsumingOrder() {
        ingredientOrders = new HashMap<>();
    }

    public Collection<IngredientOrder> allIngredients() {
        return allIngredients != null ? allIngredients : (allIngredients = ingredientOrders.values());
    }
}
