package stonering.entity.item.selectors;

import stonering.entity.crafting.IngredientOrder;
import stonering.entity.item.Item;

/**
 * Item selector to choose item for crafting order.
 * TODO add configurable item type, material and origin.
 *
 * @author Alexander on 03.09.2019.
 */
public class IngredientOrderItemSelector extends ItemSelector {
    private IngredientOrder order;

    public IngredientOrderItemSelector(IngredientOrder order) {
        this.order = order;
    }

    @Override
    public boolean checkItem(Item item) {
        return item.tags.contains(order.ingredient.tag)
                && (order.ingredient.itemTypes.contains("any")
                || order.ingredient.itemTypes.contains(item.getType().name));
    }
}
