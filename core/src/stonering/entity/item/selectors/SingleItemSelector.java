package stonering.entity.item.selectors;

import stonering.entity.item.Item;

import java.util.Collection;

/**
 * Selects single item.
 *
 * @author Alexander Kuzyakov on 22.09.2018.
 */
public abstract class SingleItemSelector extends ItemSelector {

    public Item selectItem(Collection<Item> items) {
        return items.stream().filter(this::checkItem).findFirst().orElse(null);
    }
}
