package stonering.objects.jobs.actions.aspects.target;

import stonering.global.utils.Position;
import stonering.objects.jobs.actions.Action;
import stonering.objects.local_actors.items.Item;

/**
 * @author Alexander Kuzyakov on 28.01.2018.
 *
 * targets action to item
 */
public class ItemTargetAspect extends TargetAspect {
    private Item item;

    public ItemTargetAspect(Action action, Item item) {
        super(action, true, false);
        this.item = item;
        exactTarget = true;
    }

    public Item getItem() {
        return item;
}

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public Position getTargetPosition() {
        return item.getPosition();
    }
}
