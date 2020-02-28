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
 * Can accept items and create new buttons for them. Can have logic for handling button presses, and
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
        if (map.containsKey(key)) { // update button
            StackedItemSquareButton button = map.get(key);
            button.items.add(item);
            button.updateLabel();
        } else { // create new button
            addButton(new StackedItemSquareButton(item, DrawableMap.getTextureDrawable("ui/item_slot.png")));
        }
    }

    /**
     * Divides items into groups by type and material and creates button for each group.
     * Considers that no buttons are added yet.
     */
    default void refillItems(List<Item> items) {
        Map<ItemGroupingKey, StackedItemSquareButton> map = getButtonMap();
        items.stream()
                .collect(Collectors.groupingBy(ItemGroupingKey::new)) // group by keys
                .values().stream()
                .map(item -> new StackedItemSquareButton(item, DrawableMap.getTextureDrawable("ui/item_slot.png")))
                .forEach(this::addButton);
    }

    default void addButton(StackedItemSquareButton button) {
        System.out.println("creating button for items");
        getButtonMap().put(new ItemGroupingKey(button.items), button);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                processButtonPress(button);
                if (button.items.isEmpty()) buttonEmpty(button);
            }
        });
    }

    /**
     * Called, when button goes empty.
     */
    void buttonEmpty(StackedItemSquareButton button);

    /**
     * Called on button press.
     */
    void processButtonPress(StackedItemSquareButton button);

    /**
     * Implementation should have field for button mapping.
     */
    Map<ItemGroupingKey, StackedItemSquareButton> getButtonMap();
}
