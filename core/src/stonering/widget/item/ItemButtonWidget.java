package stonering.widget.item;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.entity.item.Item;
import stonering.entity.item.ItemGroupingKey;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Interface for widgets that holds logic for grouping items into buttons by types and materials.
 * Can accept items and create new buttons for them. Can have logic for handling button presses, and 
 *
 * @author Alexander on 26.02.2020.
 */
public interface ItemButtonWidget {

    /**
     * Implementing class should have field for button mapping.
     */
    Map<ItemGroupingKey, StackedItemSquareButton> getButtonMap();

    /**
     * Called, when new button is created.
     */
    void buttonAdded(StackedItemSquareButton button);

    /**
     * Called, when button goes empty.
     */
    void buttonEmpty(StackedItemSquareButton button);

    /**
     * Called on button press.
     */
    void processButtonPress(StackedItemSquareButton button);
    
    default void addItem(Item item) {
        ItemGroupingKey key = new ItemGroupingKey(item);
        Map<ItemGroupingKey, StackedItemSquareButton> map = getButtonMap();
        if(map.containsKey(key)) { // update button
            StackedItemSquareButton button = map.get(key);
            button.items.add(item);
            button.updateLabel();
        } else { // create new button
            StackedItemSquareButton button = new StackedItemSquareButton(item);
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    processButtonPress(button);
                    if(button.items.isEmpty()) buttonEmpty(button);
                }
            });
            map.put(key, button);
            buttonAdded(button);
        }
    }
    
    /**
     * Divides items into groups by type and material and creates button for each group.
     * Considers that no buttons are added yet. 
     */
    default void fillItems(List<Item> items) {
        items.stream().collect(Collectors.groupingBy(item -> item.type)) // split by type
                .values().stream() // lists of items with same type
                .map(Collection::stream)
                .map(stream -> stream.collect(Collectors.groupingBy(item -> item.material)).values()) // split by material
                .flatMap(Collection::stream) // lists of items with same type and material
                .filter(list -> !list.isEmpty())
                .forEach(this::addItemButton);
    }

    /**
     * Adds list of items to widget. Always creates new button.
     * All items should have save type and material.
     */
    private void addItemButton(List<Item> items) {
        StackedItemSquareButton button = new StackedItemSquareButton(items);
        getButtonMap().put(new ItemGroupingKey(items), button);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                processButtonPress(button);
                if(button.items.isEmpty()) buttonEmpty(button);
            }
        });
        buttonAdded(button);
    }
}
