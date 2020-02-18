package stonering.stage.building;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.entity.item.Item;
import stonering.enums.items.recipe.Ingredient;
import stonering.game.model.system.item.ItemsStream;
import stonering.util.geometry.Position;
import stonering.util.global.CompatibleArray;
import stonering.widget.lists.NavigableList;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Table holds grid of buttons of all items, available for {@link Ingredient}.
 * @author Alexander on 17.02.2020
 */
public class RightSection extends Table {
    private NavigableList<ItemsListEntry> list;

    public RightSection() {
        add(list = new NavigableList<>());
    }

    public void fillForIngredient(Ingredient ingredient, Position position) {
        // create buttons
        List<ItemsListEntry> entries = new ItemsStream()
                .filterByReachability(position)
                .filterHasTag(ingredient.tag)
                .filterByTypes(ingredient.itemTypes)
                .stream.collect(Collectors.groupingBy(item -> item.type)) // split by type
                .values().stream() // lists of items with same type
                .map(Collection::stream)
                .map(stream -> stream.collect(Collectors.groupingBy(item -> item.material)).values()) // split by material
                .flatMap(Collection::stream) // lists of items with same type and material
                .filter(list -> !list.isEmpty())
                .map(list -> new ItemsListEntry(list.get(0).updateTitle(), list))
                .collect(Collectors.toList());
        list.setItems(new CompatibleArray<>(entries));
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
