package stonering.game.core.view.ui_components.lists;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.Array;
import javafx.util.Pair;
import stonering.enums.materials.MaterialMap;
import stonering.objects.local_actors.items.Item;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * List items and groups them by title and material.
 *
 * @author Alexander Kuzyakov on 26.06.2018
 */
public class ItemsCountList extends NavigableList {

    public ItemsCountList() {
        super();
    }

    @Override
    public void select() {
    }

    public void addItems(ArrayList<Item> items) {
        Array<ListItem> itemsArray = new Array<>();
        HashMap<Pair<String, Integer>, Integer> map = new HashMap<>();
        items.forEach(item -> {
            Pair<String, Integer> pair = new Pair<>(item.getTitle(), item.getMaterial());
            if (map.keySet().contains(pair)) {
                map.put(pair, map.get(pair) + 1);
            }
            map.put(pair, 1);
        });
        MaterialMap materialMap = MaterialMap.getInstance();
        map.keySet().forEach(key -> {
            itemsArray.add(new ListItem(materialMap.getMaterial(key.getValue()).getName(), key.getKey(), map.get(key)));
        });
        this.setItems(itemsArray);
    }

    private class ListItem {
        String title;
        String material;
        int number;

        public ListItem(String material, String title, int number) {
            this.title = title;
            this.number = number;
        }

        @Override
        public String toString() {
            return material + " " + title + " " + number;
        }
    }
}
