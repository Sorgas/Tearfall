package stonering.entity.job.action.target;

import stonering.entity.item.Item;
import stonering.enums.action.ActionTargetTypeEnum;

/**
 * Targets action to item, unit should reach item exact position to act.
 *
 * @author Alexander Kuzyakov on 28.01.2018.
 */
public class ItemActionTarget extends EntityActionTarget {

    public ItemActionTarget(Item item) {
        super(item, ActionTargetTypeEnum.EXACT);
    }

    public Item getItem() {
        return (Item) entity;
    }

    public void setItem(Item item) {
        entity = item;
    }
}
