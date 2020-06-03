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
    private final boolean acceptAny;

    public IngredientOrderItemSelector(IngredientOrder order) {
        this.order = order;
        acceptAny = order.ingredient.itemTypes.contains("any");
    }

    @Override
    public boolean checkItem(Item item) {
        System.out.println("checking item " + item + " " + item.tags + " " + acceptAny);
        return item.tags.contains(order.ingredient.tag)
                && (acceptAny || order.ingredient.itemTypes.contains(item.type.name));
    }
}
