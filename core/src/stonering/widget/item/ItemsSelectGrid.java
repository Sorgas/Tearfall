package stonering.widget.item;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import stonering.entity.item.Item;
import stonering.enums.items.recipe.Ingredient;
import stonering.game.model.system.item.ItemsStream;
import stonering.util.geometry.Position;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Widget that shows buttons of items organized in grid.
 * Is a {@link VerticalGroup} of {@link HorizontalGroup}s.
 * Items can only be added to this widget.
 * TODO add item stats tooltip
 *
 * @author Alexander on 17.02.2020
 */
public class ItemsSelectGrid extends VerticalGroup {
    private int xSize;
    private HorizontalGroup group;
    private List<StackedItemSquareButton> buttons;

    public ItemsSelectGrid(int xSize) {
        this.xSize = xSize;
        addActor(group = new HorizontalGroup());
        buttons = new ArrayList<>();
        expand();
        fill();
        align(Align.top);
        this.columnAlign(Align.left);
        group.align(Align.left);
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

    /**
     * Sets handler for button presses. All button logic should be in handler.
     */
    public void setListener(Consumer<List<Item>> consumer) {
        buttons.forEach(button -> button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                consumer.accept(button.items);
            }
        }));
    }

    private void addItemButton(List<Item> items) {
        StackedItemSquareButton button = new StackedItemSquareButton(items);
        button.pad(5);
        buttons.add(button);
        group.addActor(button);
    }
}
