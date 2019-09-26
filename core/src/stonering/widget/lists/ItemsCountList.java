package stonering.widget.lists;

import stonering.game.controller.controllers.toolbar.DesignationsController;
import stonering.entity.item.Item;
import stonering.widget.NavigableVerticalGroup;

import java.util.HashMap;
import java.util.List;

/**
 * Lists items and groups them by name and material.
 * List lines are linked to arrays of item for passing them to {@link DesignationsController}.
 *
 * @author Alexander Kuzyakov on 26.06.2018
 */
public abstract class ItemsCountList extends NavigableVerticalGroup {

    /**
     * Groups given item by title and stores them as ItemCards.
     */
    public void addItems(List<Item> items) {
        HashMap<String, ItemCardButton> map = new HashMap<>(); // item title to itemCard
        items.forEach(item -> { // groups item
            item.updateTitle();
            map.put(item.getTitle(), map.getOrDefault(item.getTitle(), new ItemCardButton(item, 0)).increment()); // count items of same type and material
        });
        map.values().forEach(card -> addActor(card));
    }
}
