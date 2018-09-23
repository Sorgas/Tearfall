package stonering.objects.local_actors.items.selectors;

import stonering.objects.local_actors.items.Item;

import java.util.ArrayList;

/**
 * Selects single item.
 *
 * @author Alexander on 22.09.2018.
 */
public abstract class SingleItemSelector extends ItemSelector {

    @Override
    public final ArrayList<Item> selectItems(ArrayList<Item> items) {
        ArrayList<Item> list = new ArrayList<>();
        list.add(selectItem(items));
        return list;
    }

    public abstract Item selectItem(ArrayList<Item> items);
}
