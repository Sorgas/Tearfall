package stonering.widget.item;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.entity.item.Item;
import stonering.enums.items.recipe.Ingredient;
import stonering.util.global.StaticSkin;

import java.util.HashMap;
import java.util.Map;

/**
 * Widget for selecting items for {@link Ingredient}.
 * Shows some title, number and category of required items, and number and types of selected items.
 * Shows selected items in a row of {@link StackedItemSquareButton}. Clicks on a button remove items from selection.
 *
 * @author Alexander on 17.02.2020
 */
public class SelectedMaterialsWidget extends Table {
    private Label quantityLabel;
    public final Ingredient ingredient;
    private int number = 0;
    private final int targetNumber;
    HorizontalGroup group;
    private final Map<String, StackedItemSquareButton> buttonMap;
    
    public SelectedMaterialsWidget(Ingredient ingredient, int targetNumber, String partName) {
        this.ingredient = ingredient;
        this.targetNumber = targetNumber;
        buttonMap = new HashMap<>();
        add(new Label(partName + ":", StaticSkin.getSkin()));
        add(new Label(ingredient.text, StaticSkin.getSkin())).right().expandX().row();
        add(quantityLabel = new Label("", StaticSkin.getSkin())).left().row();
        updateNumberLabel();
        add(group = new HorizontalGroup().left()).fillX();
    }

    public void addItem(Item item) {
        String typeName = item.type.name;
        if (!ingredient.itemTypes.contains(typeName) || !item.tags.contains(ingredient.tag)) return;
        buttonMap.putIfAbsent(typeName, new StackedItemSquareButton(item.type));
        StackedItemSquareButton button = buttonMap.get(typeName);
        button.number++;
        number++;
        updateNumberLabel();
    }
    
    private void updateNumberLabel() {
        quantityLabel.setText(number + " / " + targetNumber);
    }
}
