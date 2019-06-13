package stonering.entity.local.item.selectors;


import stonering.entity.local.item.Item;
import java.util.List;

/**
 * Class for selecting one or many item from given collection.
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
     * Selects sublist of appropriate item.
     *
     * @param items
     * @return collection of item;
     */
    public abstract List<Item> selectItems(List<Item> items);
}
