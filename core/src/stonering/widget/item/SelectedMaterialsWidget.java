package stonering.widget.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.entity.item.Item;
import stonering.entity.item.ItemGroupingKey;
import stonering.enums.items.recipe.Ingredient;
import stonering.stage.building.BuildingMaterialSelectMenu;
import stonering.util.global.StaticSkin;

import java.util.ArrayList;
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
    private final BuildingMaterialSelectMenu menu;
    public final Ingredient ingredient;
    public final String partName;
    public final int targetNumber;
    public int number = 0;

    public HorizontalGroup group;
    private Label quantityLabel;
    private final Map<ItemGroupingKey, StackedItemSquareButton> buttonMap;

    public SelectedMaterialsWidget(Ingredient ingredient, int targetNumber, String partName, BuildingMaterialSelectMenu menu) {
        this.partName = partName;
        this.ingredient = ingredient;
        this.targetNumber = targetNumber;
        this.menu = menu;
        buttonMap = new HashMap<>();
        add(new Label(partName + ":", StaticSkin.getSkin()));
        add(new Label(ingredient.text, StaticSkin.getSkin())).right().expandX().row();
        add(quantityLabel = new Label("", StaticSkin.getSkin())).left().row();
        updateNumberLabel();
        add(group = new HorizontalGroup().left()).fillX();
    }

    @Override
    public void buttonEmpty(StackedItemSquareButton button) {
        group.removeActor(button);
    }

    @Override
    public void processButtonPress(StackedItemSquareButton button) {
        System.out.println("press on button in left widget");
        int numberToDeselect = Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)
                ? 1 // add by one
                : Math.min(targetNumber - number, button.items.size()); // add max possible
        List<Item> itemsToMove = new ArrayList<>(button.items.subList(0, numberToDeselect));
        button.items.removeAll(itemsToMove); // remove from button
        button.updateLabel();
        itemsToMove.forEach(menu.rightSection.grid::addItem);
        number -= itemsToMove.size();
    }

    @Override
    public void addButton(StackedItemSquareButton button) {
        ItemButtonWidget.super.addButton(button);
        group.addActor(button);
    }

    private void updateNumberLabel() {
        quantityLabel.setText(number + " / " + targetNumber);
    }

    @Override
    public Map<ItemGroupingKey, StackedItemSquareButton> getButtonMap() {
        return buttonMap;
    }

    public List<Item> removeItemsFromButtons(int requestedNumber) {
        List<Item> items = new ArrayList<>();
        while(items.size() < requestedNumber || this.number > 0) {
            int neededNumber = requestedNumber - items.size();
            StackedItemSquareButton firstButton = ((StackedItemSquareButton) group.getChildren().items[0]);
            List<Item> itemsToMove = firstButton.items.subList(0, neededNumber);
            items.addAll(itemsToMove);
            firstButton.items.removeAll(itemsToMove);
        }
        return items;
    }
}
