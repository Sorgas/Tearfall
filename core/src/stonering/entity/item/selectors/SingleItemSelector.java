package stonering.entity.item.selectors;

import stonering.entity.item.Item;

import java.util.Arrays;
import java.util.List;

/**
 * Selects single item.
 *
 * @author Alexander Kuzyakov on 22.09.2018.
 */
public abstract class SingleItemSelector extends ItemSelector {

    @Override
    public final List<Item> selectItems(List<Item> items) {
        return Arrays.asList(selectItem(items));
    }

    public abstract Item selectItem(List<Item> items);
}
