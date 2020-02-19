package stonering.widget.item;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import stonering.entity.item.Item;
import stonering.enums.items.recipe.Ingredient;
import stonering.enums.items.type.ItemType;
import stonering.game.model.system.item.ItemsStream;
import stonering.util.geometry.Position;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Shows buttons of items. Buttons are organized in a rows.
 * Is a table of {@link HorizontalGroup}s.
 *
 * @author Alexander on 17.02.2020
 */
public class ItemsSelectGrid extends VerticalGroup {
    private int xSize;
    private int ySize;

    public ItemsSelectGrid(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        for (int y = 0; y < ySize; y++) {
            HorizontalGroup group = new HorizontalGroup();
            for (int x = 0; x < xSize; x++) {
//                group.addActor(null);
            }
            addActor(group);
        }
    }

    /**
     * Divides items into groups by type and material and creates button for each group.
     */
    public void fillItems(List<Item> items) {
        items.stream().collect(Collectors.groupingBy(item -> item.type)) // split by type
                .values().stream() // lists of items with same type
                .map(Collection::stream)
                .map(stream -> stream.collect(Collectors.groupingBy(item -> item.material)).values()) // split by material
                .flatMap(Collection::stream) // lists of items with same type and material
                .filter(list -> !list.isEmpty())
                .forEach(buttonItems -> { // create buttons
                    Item item = buttonItems.get(0);
                    addItemButton(item.type, item.material, buttonItems.size());
                });
    }

    public void fillFromIngredient(Ingredient ingredient, Position position) {
        fillItems(new ItemsStream()
                .filterByReachability(position)
                .filterHasTag(ingredient.tag)
                .filterByTypes(ingredient.itemTypes).toList());
    }

    private void addItemButton(ItemType type, int material, int number) {

    }
    
    
}
