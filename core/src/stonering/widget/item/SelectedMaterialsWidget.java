package stonering.widget.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.entity.item.Item;
import stonering.entity.item.ItemGroupingKey;
import stonering.enums.images.DrawableMap;
import stonering.enums.items.recipe.Ingredient;
import stonering.stage.entity_menu.building.BuildingMaterialSelectMenu;
import stonering.util.lang.StaticSkin;
import stonering.widget.util.HighlightHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Widget for selecting items for {@link Ingredient}.
 * Shows some title, number and category of required items, and number and types of selected items.
 * Shows selected items in a row of {@link StackedItemSquareButton}. Clicks on a button removes items from selection.
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

    public final Label titleLabel;
    private final Label quantityLabel;
    public final HorizontalGroup group;
    private final Map<ItemGroupingKey, StackedItemSquareButton> buttonMap;
    private final HighlightHandler highlightHandler;

    public SelectedMaterialsWidget(Ingredient ingredient, int targetNumber, String partName, BuildingMaterialSelectMenu menu) {
        super(StaticSkin.getSkin());
        this.partName = partName;
        this.ingredient = ingredient;
        this.targetNumber = targetNumber;
        this.menu = menu;
        buttonMap = new HashMap<>();
        pad(5);
        add(titleLabel = new Label(partName + ":", StaticSkin.getSkin()));
        add(new Label("ingredient title here", StaticSkin.getSkin())).right().expandX().row();
        add(quantityLabel = new Label("0 / " + targetNumber, StaticSkin.getSkin())).left().row();
        add(group = new HorizontalGroup().left()).height(SingleItemSquareButton.SIZE).fillX().colspan(2);
        highlightHandler = new HighlightHandler() {
            @Override
            protected void apply(boolean value) {
                setBackground(DrawableMap.REGION.getDrawable("materials_select_widget" + (value ? ":focused" : "")));
            }
        };
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        highlightHandler.accept(menu.leftSection.group.getSelectedElement() == this);
    }

    @Override
    public void itemAdded(StackedItemSquareButton button, Item item) {
        updateMenuState(1);
    }

    @Override
    public void buttonAdded(StackedItemSquareButton button) {
        ItemButtonWidget.super.removeButton(button);
        group.addActor(button);
    }

    @Override
    public void buttonEmpty(StackedItemSquareButton button) {
        group.removeActor(button);
    }

    @Override
    public void processButtonPress(StackedItemSquareButton button) {
        int numberToDeselect = Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)
                ? 1 // add by one
                : button.items.size(); // add max possible
        List<Item> itemsToMove = new ArrayList<>(button.items.subList(0, numberToDeselect));
        button.items.removeAll(itemsToMove); // remove from button
        button.updateLabel();
        GroupedItemSelectGrid groupedItemSelectGrid = menu.rightSection.grid;
        for (int i = 0; i < itemsToMove.size(); i++) {
            Item item = itemsToMove.get(i);
            item.locked = false;
            groupedItemSelectGrid.addItem(item);
        }
        updateMenuState(-itemsToMove.size());
    }

    private void updateMenuState(int delta) {
        number += delta;
        quantityLabel.setText(number + " / " + targetNumber); // update label
        menu.rightSection.grid.setAllButtonsDisabled(number == targetNumber); // lock grid if widget is full
    }

    @Override
    public Map<ItemGroupingKey, StackedItemSquareButton> getButtonMap() {
        return buttonMap;
    }

    /**
     * Used for order creation. Selects first found items from buttons.
     */
    public List<Item> removeItemsFromButtons(int requestedNumber) {
        List<Item> items = new ArrayList<>();
        while (items.size() < requestedNumber || this.number > 0) {
            int neededNumber = requestedNumber - items.size();
            StackedItemSquareButton firstButton = ((StackedItemSquareButton) group.getChildren().items[0]);
            List<Item> itemsToMove = firstButton.items.subList(0, neededNumber);
            items.addAll(itemsToMove);
            firstButton.items.removeAll(itemsToMove);
        }
        return items;
    }
}
