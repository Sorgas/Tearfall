package stonering.widget.lists;

import stonering.entity.item.Item;
import stonering.util.logging.Logger;
import stonering.widget.NavigableVerticalGroup;
import stonering.widget.item.ItemCardButton;

import java.util.HashMap;
import java.util.List;

/**
 * Lists items and groups them by name and material.
 * List lines are linked to arrays of items.
 *
 * @author Alexander Kuzyakov on 26.06.2018
 */
public abstract class ItemsCountList extends NavigableVerticalGroup {

    /**
     * Groups given items by title and stores them as ItemCards.
     */
    public void addItems(List<Item> items) {
        HashMap<String, ItemCardButton> map = new HashMap<>(); // item title to itemCard
        Logger.BUILDING.logDebug("Grouping " + items.size() + " to numbered list.");
        items.forEach(item -> { // groups item
            String title = item.title;
            map.put(title, map.getOrDefault(title, new ItemCardButton(item, 0)).increment()); // count items of same type and material
        });
        map.values().forEach(this::addActor);
        Logger.BUILDING.logDebug(map.values().size() + " cards created.");
    }
}
