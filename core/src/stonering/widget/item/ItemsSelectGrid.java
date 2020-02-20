package stonering.widget.item;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import stonering.entity.item.Item;
import stonering.enums.items.recipe.Ingredient;
import stonering.game.model.system.item.ItemsStream;
import stonering.util.geometry.Position;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Shows buttons of items. Buttons are organized in a rows.
 * Is a {@link VerticalGroup} of {@link HorizontalGroup}s.
 *
 * @author Alexander on 17.02.2020
 */
public class ItemsSelectGrid extends VerticalGroup {
    private int xSize;

    public ItemsSelectGrid(int xSize) {
        this.xSize = xSize;
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
                .forEach(this::addItemButton);
    }

    public void fillForIngredient(Ingredient ingredient, Position position) {
        fillItems(new ItemsStream()
                .filterByReachability(position)
                .filterHasTag(ingredient.tag)
                .filterByTypes(ingredient.itemTypes).toList());
    }

    private void addItemButton(List<Item> items) {
        getGroupForAdding().addActor(new StackedItemSquareButton(items));
    }
    
    private HorizontalGroup getGroupForAdding() {
        Actor[] groups = getChildren().items;
        if(groups.length > 0) {
            HorizontalGroup lastGroup = (HorizontalGroup) groups[groups.length - 1];
            if (lastGroup.getChildren().size != xSize) return lastGroup;
        }
        return addRow();
    }
    
    private HorizontalGroup addRow() {
        HorizontalGroup group = new HorizontalGroup();
        addActor(group);
        return group;
    }
}
