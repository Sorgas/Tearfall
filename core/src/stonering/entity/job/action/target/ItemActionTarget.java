package stonering.entity.job.action.target;

import stonering.entity.item.Item;

/**
 * Targets name to item
 *
 * @author Alexander Kuzyakov on 28.01.2018.
 */
public class ItemActionTarget extends EntityActionTarget {

    public ItemActionTarget(Item item) {
        super(item, ActionTarget.EXACT);
    }

    public Item getItem() {
        return (Item) entity;
    }

    public void setItem(Item item) {
        entity = item;
    }
}
