package stonering.widget.item;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import stonering.entity.item.Item;
import stonering.entity.item.ItemGroupingKey;
import stonering.enums.items.recipe.Ingredient;
import stonering.game.model.system.item.ItemsStream;
import stonering.util.geometry.Position;

import java.util.*;
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
public class ItemsSelectGrid extends ActorGrid<StackedItemSquareButton> {
    private Map<ItemGroupingKey, StackedItemSquareButton> buttonMap;
    public Consumer<StackedItemSquareButton> commonHandler; // handles all buttons
    
    public ItemsSelectGrid(int cellWidth, int cellHeight) {
        super(cellWidth, cellHeight);
        buttonMap = new HashMap<>();
        commonHandler = button -> {};
        defaults().pad(5).size(StackedItemSquareButton.SIZE, StackedItemSquareButton.SIZE);
        top().left();
        super.init();
        // set table background
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
                .stream.filter(ingredient::checkItem).collect(Collectors.toList()));
    }
    
    public void setAllButtonsDisabled(boolean disabled) {
        for (Cell<StackedItemSquareButton>[] row : gridCells) {
            for (Cell<StackedItemSquareButton> cell : row) {
                if(cell != null) cell.getActor().setDisabled(disabled);
            }
        }
    }

    /**
     * Adds list of items to widget. Always creates new button.
     * All items should have save type and material.
     */
    private void addItemButton(List<Item> items) {
        StackedItemSquareButton button = new StackedItemSquareButton(items);
        buttonMap.put(new ItemGroupingKey(items), button);
        addActorToGrid(button);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                commonHandler.accept(button);
            }
        });
    }

    /**
     * Adds another item to widget. Creates new button or updates existing one.
     */
    public void addItem(Item item) {
        ItemGroupingKey key = new ItemGroupingKey(item);
        StackedItemSquareButton button = buttonMap.get(key);
        if(button == null) {
            button = new StackedItemSquareButton(item);
            buttonMap.put(key, button);
            addActorToGrid(button);
        } else {
            button.items.add(item);
            button.updateLabel();
        }
    }
}
