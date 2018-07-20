package stonering.game.core.view.ui_components.lists;

import stonering.enums.materials.MaterialMap;
import stonering.game.core.GameMvc;
import stonering.game.core.controller.controllers.DesignationsController;
import stonering.objects.local_actors.items.Item;
import stonering.utils.global.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * List items and groups them by title and material.
 * List lines are linked to arrays of items for passing them to {@link DesignationsController}.
 *
 * @author Alexander Kuzyakov on 26.06.2018
 */
public class ItemsCountList extends NavigableList {

    public ItemsCountList(GameMvc gameMvc, boolean hideable) {
        super(gameMvc, hideable);
    }

    @Override
    public void select() {
    }

    public void addItems(List<Item> items) {
        HashMap<Pair<String, Integer>, ListItem> map = new HashMap<>();
        MaterialMap materialMap = MaterialMap.getInstance();
        items.forEach(item -> {
            Pair<String, Integer> pair = new Pair<>(item.getTitle(), item.getMaterial());
            ListItem listItem;
            if (map.keySet().contains(pair)) {
                listItem = map.get(pair);
                listItem.number++;
            } else {
                listItem = new ListItem(materialMap.getMaterial(item.getMaterial()).getName(), item.getTitle(), 1);
                map.put(pair, listItem);
            }
            listItem.items.add(item);
        });
        this.setItems(map.values());
    }

    private class ListItem {
        String title;
        String material;
        int number;
        ArrayList<Item> items;

        public ListItem(String material, String title, int number) {
            this.material = material;
            this.title = title;
            this.number = number;
            items = new ArrayList<>();
        }

        @Override
        public String toString() {
            return material + " " + title + " " + number;
        }
    }
}
