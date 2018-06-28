package stonering.game.core.view.ui_components.menus;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.Array;
import stonering.utils.global.StaticSkin;

import java.util.HashMap;

/**
 * @author Alexander Kuzyakov
 * created on 26.06.2018
 */
public class MaterialSelectList extends List {

    public MaterialSelectList() {
        super(StaticSkin.getSkin());
    }

    public void addItems(HashMap<String, Integer> items) {
        Array<ListItem> itemsArray = new Array<>();
        items.forEach((key, value) -> itemsArray.add(new ListItem(key, value)));
        this.setItems(itemsArray);
    }

    private class ListItem {
        String title;
        int number;

        public ListItem(String title, int number) {
            this.title = title;
            this.number = number;
        }

        @Override
        public String toString() {
            return title + " " + number;
        }
    }
}
