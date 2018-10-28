package stonering.exceptions;

import stonering.entity.local.crafting.ItemOrder;

/**
 * Is thrown by consumers of item item orders ({@link stonering.generators.items.ItemGenerator}) if order is invalid.
 *
 * @author Alexander on 28.10.2018.
 */
public class InvalidCraftinOrder extends Exception {
    private ItemOrder order;

    public InvalidCraftinOrder(ItemOrder order) {
        this.order = order;
    }
}
