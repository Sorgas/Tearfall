package stonering.entity.crafting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Superclass for {@link ItemOrder} and {@link stonering.entity.building.BuildingOrder}.
 * Stores map of {@link IngredientOrder}s.
 *
 * @author Alexander on 05.05.2020
 */
public class ItemConsumingOrder {
    public final Map<String, List<IngredientOrder>> ingredientOrders;
    private List<IngredientOrder> allIngredients;

    public ItemConsumingOrder() {
        ingredientOrders = new HashMap<>();
    }

    public List<IngredientOrder> allIngredients() {
        return allIngredients != null
                ? allIngredients
                : (allIngredients = ingredientOrders.values().stream().flatMap(List::stream).collect(Collectors.toList()));
    }
}
