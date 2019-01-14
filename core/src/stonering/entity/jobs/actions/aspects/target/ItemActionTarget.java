package stonering.entity.jobs.actions.aspects.target;

import stonering.util.geometry.Position;
import stonering.entity.jobs.actions.Action;
import stonering.entity.local.items.Item;

/**
 * Targets action to item
 *
 * @author Alexander Kuzyakov on 28.01.2018.
 */
public class ItemActionTarget extends ActionTarget {
    private Item item;

    public ItemActionTarget(Action action, Item item) {
        super(action, true, false);
        this.item = item;
        exactTarget = true;
    }

    @Override
    public Position getPosition() {
        return item.getPosition();
    }

    public Item getItem() {
        return item;
}

    public void setItem(Item item) {
        this.item = item;
    }
}
