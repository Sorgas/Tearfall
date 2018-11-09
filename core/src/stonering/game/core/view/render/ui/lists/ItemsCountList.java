package stonering.game.core.view.render.ui.lists;

import com.badlogic.gdx.utils.Array;
import stonering.enums.materials.MaterialMap;
import stonering.game.core.GameMvc;
import stonering.game.core.controller.controllers.toolbar.DesignationsController;
import stonering.entity.local.items.Item;
import stonering.utils.global.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * List items and groups them by title and material.
 * List lines are linked to arrays of items for passing them to {@link DesignationsController}.
 *
 * @author Alexander Kuzyakov on 26.06.2018
 */
public abstract class ItemsCountList extends NavigableList {

    public ItemsCountList(GameMvc gameMvc, boolean hideable) {
        super(gameMvc, hideable);
    }

    /**
     * Groups given items by name and material and stores them as ListItems.
     *
     * @param items
     */
    public void addItems(List<Item> items) {
        HashMap<Pair<String, Integer>, ListItem> map = new HashMap<>(); // item name & material to ListItem
        MaterialMap materialMap = MaterialMap.getInstance();
        items.forEach(item -> { // groups items
            Pair<String, Integer> pair = new Pair<>(item.getName(), item.getMaterial());
            if (map.keySet().contains(pair)) {
                map.get(pair).items.add(item);
            } else {
                map.put(pair, new ListItem(item.getTitle(), materialMap.getMaterial(item.getMaterial()).getName(), item));
            }
        });
        Array<ListItem> listItems = new Array<>();
        listItems.addAll(map.values().toArray(new ListItem[1]));
        this.setItems(listItems);
    }

    protected ListItem getSelectedListItem() {
        return (ListItem) getSelected();
    }

    /**
     * Encapsulates list of items.
     */
    protected class ListItem {
        String title;
        String material;
        List<Item> items;

        protected ListItem(String material, String title) {
            this.material = material;
            this.title = title;
            items = new ArrayList<>();
        }

        public ListItem(String title, String material, Item item) {
            this.title = title;
            this.material = material;
            this.items = Collections.singletonList(item);
        }
        @Override
        public String toString() {
            return material + " " + title + " " + items.size();
        }
    }
}
