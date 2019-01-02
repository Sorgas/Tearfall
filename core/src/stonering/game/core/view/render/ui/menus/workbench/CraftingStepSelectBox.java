package stonering.game.core.view.render.ui.menus.workbench;

import stonering.game.core.view.render.ui.lists.NavigableSelectBox;
import stonering.game.core.view.render.ui.util.ListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Select box for workbench screen.
 *
 * @author Alexander on 25.11.2018.
 */
public class CraftingStepSelectBox extends NavigableSelectBox {
    private Map<String, ListItem> items;

    public CraftingStepSelectBox() {
        super();
        items = new HashMap<>();
    }

    public void setListItems(List<ListItem> list) {
        ArrayList<String> strings = new ArrayList<>();
        list.forEach(listItem -> {
            String key = makeListString(listItem);
            items.put(key, listItem);
            strings.add(key);
        });
        this.setItems(strings);
    }

    private String makeListString(ListItem listItem) {
        return listItem.getTitle() + " " + listItem.getMaterial();
    }
}
