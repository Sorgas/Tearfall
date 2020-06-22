package stonering.entity.item.selectors;

import stonering.entity.item.Item;

import java.util.Collection;

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
    public boolean checkItem(Item item) {
        return item == this.item;
    }

    @Override
    public Item selectItem(Collection<Item> items) {
        return checkItems(items) ? item : null;
    }
}
