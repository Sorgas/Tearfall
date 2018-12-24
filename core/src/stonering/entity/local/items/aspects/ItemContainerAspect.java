package stonering.entity.local.items.aspects;

import stonering.entity.local.Aspect;
import stonering.entity.local.AspectHolder;
import stonering.entity.local.items.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Entities with this aspect can hold items.
 */
public class ItemContainerAspect extends Aspect {
    public static String NAME = "item_container";
    private List<Item> items;

    public ItemContainerAspect(AspectHolder aspectHolder) {
        super(aspectHolder);
        items = new ArrayList<>();
    }

    @Override
    public void turn() {
        items.forEach(AspectHolder::turn);
    }

    public List<Item> getItems() {
        return items;
    }
}
