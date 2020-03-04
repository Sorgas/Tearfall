package stonering.widget.item;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.entity.item.Item;
import stonering.entity.item.ItemGroupingKey;
import stonering.enums.images.DrawableMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Interface for widgets that holds logic for grouping items into buttons by types and materials.
 * Controls map of buttons, provided by implementation.
 * Items can be added or removed from widget. Adding can create or update a button.
 * Removing can update a button or trigger buttonEmpty method.
 * Has method, called for button presses. If button goes empty after press,
 *
 * @author Alexander on 26.02.2020.
 */
public interface ItemButtonWidget {

    /**
     * Adds single item. Finds appropriate button or creates a new one.
     */
    default void addItem(Item item) {
        ItemGroupingKey key = new ItemGroupingKey(item);
        Map<ItemGroupingKey, StackedItemSquareButton> map = getButtonMap();
        StackedItemSquareButton button;
        if (map.containsKey(key)) { // update button
            button = map.get(key);
            button.addItem(item);
        } else { // create new button
            button = new StackedItemSquareButton(item, DrawableMap.getTextureDrawable("ui/item_slot.png"));
            addButton(button);
        }
        itemAdded(button, item);
    }

    /**
     * Divides items into groups by type and material and creates button for each group.
     * Considers that no buttons are added yet.
     */
    default void refillItems(List<Item> items) {
        items.stream()
                .collect(Collectors.groupingBy(ItemGroupingKey::new)) // group by keys
                .values().stream()
                .map(list -> new StackedItemSquareButton(list, DrawableMap.getTextureDrawable("ui/item_slot.png")))
                .forEach(this::addButton);
    }

    default void addButton(StackedItemSquareButton button) {
        getButtonMap().put(new ItemGroupingKey(button.items), button);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                processButtonPress(button);
                if (button.items.isEmpty()) buttonEmpty(button);
            }
        });
        buttonAdded(button); // for adding to layout
        if(button.items.isEmpty()) buttonEmpty(button);
    }

    default void removeButton(StackedItemSquareButton button) {
        getButtonMap().remove(new ItemGroupingKey(button.getItem()));
    }

    default void itemAdded(StackedItemSquareButton button, Item item) {
    }

    default void buttonAdded(StackedItemSquareButton button) {
    }

    default void buttonEmpty(StackedItemSquareButton button) {
    }

    default void processButtonPress(StackedItemSquareButton button) {
    }

    Map<ItemGroupingKey, StackedItemSquareButton> getButtonMap();
}
