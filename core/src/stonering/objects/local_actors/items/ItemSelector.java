package stonering.objects.local_actors.items;


import java.util.ArrayList;

/**
 * @author Alexander on 21.07.2018.
 */
public abstract class ItemSelector {
    public abstract boolean check(ArrayList<Item> items);

    /**
     *
     * @param items
     * @return null if no item can be selected;
     */
    public abstract ArrayList<Item> selectItems(ArrayList<Item> items);
}
