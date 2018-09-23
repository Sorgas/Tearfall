package stonering.objects.local_actors.items.selectors;

import stonering.objects.local_actors.items.Item;

import java.util.ArrayList;

/**
 * Selects single specific item.
 *
 * @author Alexander on 22.09.2018.
 */
public class ExactItemSelector extends SingleItemSelector {
    private Item item;

    public ExactItemSelector(Item item) {
        this.item = item;
    }

    @Override
    public boolean check(ArrayList<Item> items) {
        return items.contains(item);
    }

    @Override
    public Item selectItem(ArrayList<Item> items) {
        if(check(items)) {
            return item;
        }
        return null;
    }
}
