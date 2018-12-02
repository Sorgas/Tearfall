package stonering.game.core.view.render.ui.util;

import stonering.entity.local.items.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander on 25.11.2018.
 */
public class ListItem {
    private String title;
    private String material;
    private List<Item> items;

    public ListItem(String material, String title) {
        this.material = material;
        this.title = title;
        items = new ArrayList<>();
    }

    public ListItem(String title, String material, Item item) {
        this.title = title;
        this.material = material;
        this.items = new ArrayList<>();
        items.add(item);
    }
    @Override
    public String toString() {
        return material + " " + title + " " + items.size();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
