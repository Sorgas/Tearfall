package stonering.entity.local.items.selectors;

import stonering.entity.local.items.Item;

import java.util.List;

/**
 * Selects single specific item.
 *
 * @author Alexander Kuzyakov on 22.09.2018.
 */
public class ExactItemSelector extends SingleItemSelector {
    private Item item;

    public ExactItemSelector(Item item) {
        this.item = item;
    }

    @Override
    public boolean check(List<Item> items) {
        return items.contains(item);
    }

    @Override
    public Item selectItem(List<Item> items) {
        return check(items) ? item : null;
    }
}
