package stonering.entity.job.action.target;

import stonering.util.geometry.Position;
import stonering.entity.item.Item;

/**
 * Targets name to item
 *
 * @author Alexander Kuzyakov on 28.01.2018.
 */
public class ItemActionTarget extends ActionTarget {
    private Item item;

    public ItemActionTarget(Item item) {
        super(true, false);
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
