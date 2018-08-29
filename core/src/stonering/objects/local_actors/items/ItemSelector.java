package stonering.objects.local_actors.items;


import java.util.ArrayList;

/**
 * @author Alexander on 21.07.2018.
 */
public abstract class ItemSelector {
    /**
     * Checks if collection contains appropriate item.
     *
     * @param items
     * @return
     */
    public abstract boolean check(ArrayList<Item> items);

    /**
     * Selects sublist of appropriate items.
     *
     * @param items
     * @return collection of items;
     */
    public abstract ArrayList<Item> selectItems(ArrayList<Item> items);
}
