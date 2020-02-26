package stonering.widget.item;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.entity.item.Item;
import stonering.entity.item.ItemGroupingKey;
import stonering.enums.items.recipe.Ingredient;
import stonering.util.global.StaticSkin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Widget for selecting items for {@link Ingredient}.
 * Shows some title, number and category of required items, and number and types of selected items.
 * Shows selected items in a row of {@link StackedItemSquareButton}. Clicks on a button remove items from selection.
 * Items themselves are stored in buttons. Groups selected items into buttons by type and material.
 *
 * @author Alexander on 17.02.2020
 */
public class SelectedMaterialsWidget extends Table implements ItemButtonWidget {
    public final Ingredient ingredient;
    public final String partName;
    public final int targetNumber;
    public int number = 0;

    public HorizontalGroup group;
    private Label quantityLabel;
    private final Map<ItemGroupingKey, StackedItemSquareButton> buttonMap;
    
    public SelectedMaterialsWidget(Ingredient ingredient, int targetNumber, String partName) {
        this.partName = partName;
        this.ingredient = ingredient;
        this.targetNumber = targetNumber;
        buttonMap = new HashMap<>();
        add(new Label(partName + ":", StaticSkin.getSkin()));
        add(new Label(ingredient.text, StaticSkin.getSkin())).right().expandX().row();
        add(quantityLabel = new Label("", StaticSkin.getSkin())).left().row();
        updateNumberLabel();
        add(group = new HorizontalGroup().left()).fillX();
    }

    @Override
    public void buttonEmpty(StackedItemSquareButton button) {

    }

    @Override
    public void processButtonPress(StackedItemSquareButton button) {

    }

    public void addItem(Item item) {
        ItemGroupingKey key = new ItemGroupingKey(item);
        StackedItemSquareButton button = buttonMap.get(key);
        if (button == null) { // create new button
            button = new StackedItemSquareButton(item);
            button.addListener(new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    
                }
            });
            buttonMap.put(key, button);
            
        } else { // add another item to existing button
            button.items.add(item);
            button.updateLabel();
        }
        number++;
        updateNumberLabel();
    }

    private void updateNumberLabel() {
        quantityLabel.setText(number + " / " + targetNumber);
    }

    public void addItems(List<Item> items) {
        items.forEach(this::addItem);
    }

    @Override
    public Map<ItemGroupingKey, StackedItemSquareButton> getButtonMap() {
        return buttonMap;
    }

    @Override
    public void buttonAdded(StackedItemSquareButton button) {
        group.addActor(button);
    }
}
