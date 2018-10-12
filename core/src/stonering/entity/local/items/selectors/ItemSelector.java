package stonering.entity.local.items.selectors;


import stonering.entity.local.items.Item;
import java.util.List;

/**
 * Class for selecting one or many items from given collection.
 *
 * @author Alexander Kuzyakov on 21.07.2018.
 */
public abstract class ItemSelector {
    /**
     * Checks if collection contains appropriate item.
     *
     * @param items
     * @return
     */
    public abstract boolean check(List<Item> items);

    /**
     * Selects sublist of appropriate items.
     *
     * @param items
     * @return collection of items;
     */
    public abstract List<Item> selectItems(List<Item> items);
}
