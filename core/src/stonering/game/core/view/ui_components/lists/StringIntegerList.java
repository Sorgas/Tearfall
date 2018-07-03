package stonering.game.core.view.ui_components.lists;

import com.badlogic.gdx.utils.Array;
import java.util.HashMap;

/**
 * List that shows strings and integers from given map.
 *
 * @author Alexander Kuzyakov on 26.06.2018
 */
public class StringIntegerList extends NavigableList {

    public StringIntegerList() {
        super();
    }

    @Override
    public void select() {
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

    public String getSelectedString() {
        return ((ListItem) getSelected()).title;
    }
}
