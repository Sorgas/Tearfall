package stonering.entity.item.selectors;


import stonering.entity.item.Item;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class for selecting one or many item from given collection.
 *
 * @author Alexander Kuzyakov on 21.07.2018.
 */
public abstract class ItemSelector {

    /**
     * Checks if collection contains appropriate item.
     */
    public boolean checkItems(Collection<Item> items) {
        return items.stream().anyMatch(this::checkItem);
    }

    /**
     * Selects sublist of appropriate items.
     * If selector should select multiple items, but not all can be selected, this should return empty list.
     */
    public List<Item> selectItems(Collection<Item> items) {
        return items.stream().filter(this::checkItem).collect(Collectors.toList());
    }

    /**
     * Checks if given item is appropriate.
     */
    public abstract boolean checkItem(Item item);
}
