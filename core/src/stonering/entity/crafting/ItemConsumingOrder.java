package stonering.entity.crafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Superclass for {@link ItemOrder} and {@link stonering.entity.building.BuildingOrder}.
 * Stores map of {@link IngredientOrder}s.
 *
 * @author Alexander on 05.05.2020
 */
public class ItemConsumingOrder {
    public final HashMap<String, IngredientOrder> parts; //item parts to their ingredients
    public final List<IngredientOrder> consumed;
    public final List<IngredientOrder> allIngredients;
    public IngredientOrder main;

    public ItemConsumingOrder() {
        parts = new HashMap<>();
        consumed = new ArrayList<>();
        allIngredients = new ArrayList<>();
    }

    public List<IngredientOrder> allIngredients() {
        if(allIngredients.isEmpty()) {
            allIngredients.addAll(consumed);
            allIngredients.addAll(parts.values());
            if(main != null) allIngredients.add(main);
        }
        return allIngredients;
    }
}
