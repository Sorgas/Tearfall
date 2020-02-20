package stonering.stage.building;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.entity.item.Item;
import stonering.enums.items.recipe.Ingredient;
import stonering.game.model.system.item.ItemsStream;
import stonering.util.geometry.Position;
import stonering.util.global.CompatibleArray;
import stonering.widget.item.ItemsSelectGrid;
import stonering.widget.lists.NavigableList;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Table holds grid of buttons of all items, available for {@link Ingredient}.
 * @author Alexander on 17.02.2020
 */
public class RightSection extends Table {
    ItemsSelectGrid grid;
    private NavigableList<ItemsListEntry> list;

    public RightSection() {
        add(grid = new ItemsSelectGrid(8));
    }

    public void fill(Ingredient ingredient, Position position) {
        grid.fillForIngredient(ingredient, position);
    }

    private static class ItemsListEntry {
        String name;
        List<Item> items;
        int number;

        public ItemsListEntry(String name, List<Item> items) {
            this.name = name;
            this.items = items;
            number = items.size();
        }
    }
}
