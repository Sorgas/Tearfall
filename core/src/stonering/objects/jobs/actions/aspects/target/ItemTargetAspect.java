package stonering.objects.jobs.actions.aspects.target;

import stonering.global.utils.Position;
import stonering.objects.local_actors.items.Item;

/**
 * Created by Alexander on 28.01.2018.
 */
public class ItemTargetAspect extends TargetAspect {
    private Item item;

    public ItemTargetAspect(Item item) {
        this.item = item;
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

    @Override
    public boolean isExactTarget() {
        return true;
    }
}
